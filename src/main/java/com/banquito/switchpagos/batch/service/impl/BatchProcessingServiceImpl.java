package com.banquito.switchpagos.batch.service.impl;

import com.banquito.switchpagos.batch.client.CoreBankingClient;
import com.banquito.switchpagos.batch.client.CoreAccountClient;
import com.banquito.switchpagos.batch.client.CoreCustomerClient;
import com.banquito.switchpagos.batch.dto.event.PaymentLineRequestedEvent;
import com.banquito.switchpagos.batch.dto.request.CoreFundingRequest;
import com.banquito.switchpagos.batch.dto.request.ParsedBatchFile;
import com.banquito.switchpagos.batch.dto.request.ParsedPaymentLine;
import com.banquito.switchpagos.batch.dto.response.CoreCustomerResponse;
import com.banquito.switchpagos.batch.dto.response.CoreAccountResponse;
import com.banquito.switchpagos.batch.dto.response.CoreFundingResponse;
import com.banquito.switchpagos.batch.enums.BatchStatus;
import com.banquito.switchpagos.batch.enums.FundingRequestStatus;
import com.banquito.switchpagos.batch.enums.LineStatus;
import com.banquito.switchpagos.batch.exception.CoreBankingClientException;
import com.banquito.switchpagos.batch.exception.CoreAccountClientException;
import com.banquito.switchpagos.batch.exception.CoreCustomerClientException;
import com.banquito.switchpagos.batch.exception.ResourceNotFoundException;
import com.banquito.switchpagos.batch.mapper.PaymentLineMapper;
import com.banquito.switchpagos.batch.model.BatchFundingRequest;
import com.banquito.switchpagos.batch.model.BatchPaymentLine;
import com.banquito.switchpagos.batch.model.BatchValidationError;
import com.banquito.switchpagos.batch.model.PaymentBatch;
import com.banquito.switchpagos.batch.repository.BatchFundingRequestRepository;
import com.banquito.switchpagos.batch.repository.BatchPaymentLineRepository;
import com.banquito.switchpagos.batch.repository.BatchValidationErrorRepository;
import com.banquito.switchpagos.batch.repository.PaymentBatchRepository;
import com.banquito.switchpagos.batch.service.BatchProcessingService;
import com.banquito.switchpagos.batch.service.PaymentLineEventPublisher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class BatchProcessingServiceImpl implements BatchProcessingService {

    private static final Logger LOG = LoggerFactory.getLogger(BatchProcessingServiceImpl.class);
    private static final String COMPANY_INACTIVE = "COMPANY_INACTIVE";
    private static final String MASS_PAYMENTS_DISABLED = "MASS_PAYMENTS_DISABLED";
    private static final String COMPANY_CUSTOMER_RESOLUTION_FAILED = "COMPANY_CUSTOMER_RESOLUTION_FAILED";
    private static final String SOURCE_ACCOUNT_NOT_OWNED_BY_COMPANY = "SOURCE_ACCOUNT_NOT_OWNED_BY_COMPANY";
    private static final String SOURCE_ACCOUNT_INACTIVE = "SOURCE_ACCOUNT_INACTIVE";
    private static final String SOURCE_ACCOUNT_NOT_ELIGIBLE = "SOURCE_ACCOUNT_NOT_ELIGIBLE";
    private static final Set<String> VALID_ROUTING_CODES = Set.of("10", "30", "32", "35");
    private static final List<String> DUPLICATE_STATUSES = List.of(
            BatchStatus.FONDEO_SOLICITADO.name(),
            BatchStatus.FONDEADO.name(),
            BatchStatus.FRAGMENTANDO.name(),
            BatchStatus.PROCESANDO_LINEAS.name(),
            "PROCESADO_PARCIAL",
            "PROCESADO_TOTAL",
            "COMISION_CALCULADA",
            "COBRO_COMISION_SOLICITADO",
            "COBRO_COMISION_EXITOSO",
            "REPORTES_GENERADOS",
            BatchStatus.CERRADO.name());

    private final PaymentBatchRepository paymentBatchRepository;
    private final BatchValidationErrorRepository validationErrorRepository;
    private final BatchFundingRequestRepository fundingRequestRepository;
    private final BatchPaymentLineRepository paymentLineRepository;
    private final CoreBankingClient coreBankingClient;
    private final CoreCustomerClient coreCustomerClient;
    private final CoreAccountClient coreAccountClient;
    private final PaymentLineMapper paymentLineMapper;
    private final PaymentLineEventPublisher paymentLineEventPublisher;
    private final String defaultAccountingDate;
    private final String coreSwitchChannel;

    public BatchProcessingServiceImpl(
            PaymentBatchRepository paymentBatchRepository,
            BatchValidationErrorRepository validationErrorRepository,
            BatchFundingRequestRepository fundingRequestRepository,
            BatchPaymentLineRepository paymentLineRepository,
            CoreBankingClient coreBankingClient,
            CoreCustomerClient coreCustomerClient,
            CoreAccountClient coreAccountClient,
            PaymentLineMapper paymentLineMapper,
            PaymentLineEventPublisher paymentLineEventPublisher,
            @Value("${core.switch.default-accounting-date}") String defaultAccountingDate,
            @Value("${core.switch.channel}") String coreSwitchChannel) {
        this.paymentBatchRepository = paymentBatchRepository;
        this.validationErrorRepository = validationErrorRepository;
        this.fundingRequestRepository = fundingRequestRepository;
        this.paymentLineRepository = paymentLineRepository;
        this.coreBankingClient = coreBankingClient;
        this.coreCustomerClient = coreCustomerClient;
        this.coreAccountClient = coreAccountClient;
        this.paymentLineMapper = paymentLineMapper;
        this.paymentLineEventPublisher = paymentLineEventPublisher;
        this.defaultAccountingDate = defaultAccountingDate;
        this.coreSwitchChannel = coreSwitchChannel;
    }

    @Override
    @Async
    @Transactional
    public void processBatch(UUID batchId, ParsedBatchFile parsedBatchFile) {
        PaymentBatch batch = findBatch(batchId);
        LOG.info("Starting async batch processing. batchId={}, correlationId={}", batchId, batch.getCorrelationId());

        updateBatchStatus(batch, BatchStatus.VALIDANDO, null);
        List<BatchValidationError> validationErrors = validateBatch(batch, parsedBatchFile);
        if (!validationErrors.isEmpty()) {
            validationErrorRepository.saveAll(validationErrors);
            updateBatchStatus(batch, BatchStatus.RECHAZADO, "El lote no supero la validacion profunda.");
            LOG.info("Batch rejected by deep validation. batchId={}, errors={}", batchId, validationErrors.size());
            return;
        }

        batch.setValidatedAt(OffsetDateTime.now());
        updateBatchStatus(batch, BatchStatus.VALIDADO, null);

        CoreCustomerResponse company;
        try {
            company = resolveCompany(batch);
        } catch (CoreCustomerClientException exception) {
            updateBatchStatus(batch, BatchStatus.RECHAZADO, formatCompanyRejection(exception));
            LOG.info("Batch rejected during company resolution. batchId={}, code={}, httpStatus={}",
                    batchId, exception.getCode(), exception.getHttpStatus());
            return;
        }

        try {
            validateSourceAccount(batch, company);
        } catch (CoreAccountClientException exception) {
            updateBatchStatus(batch, BatchStatus.RECHAZADO, formatAccountRejection(exception));
            LOG.info("Batch rejected during source account validation. batchId={}, code={}, httpStatus={}",
                    batchId, exception.getCode(), exception.getHttpStatus());
            return;
        }

        BatchFundingRequest fundingRequest = createFundingRequest(batch);
        fundingRequestRepository.save(fundingRequest);
        updateBatchStatus(batch, BatchStatus.FONDEO_SOLICITADO, null);

        CoreFundingResponse fundingResponse = requestFunding(batch, fundingRequest, company);
        if (!isApprovedFundingStatus(fundingResponse.getStatus())) {
            fundingRequest.setRequestStatus(isTechnicalFundingError(fundingResponse.getStatus())
                    ? FundingRequestStatus.FALLIDO.name()
                    : FundingRequestStatus.RECHAZADO.name());
            fundingRequest.setCoreResponseStatus(fundingResponse.getStatus());
            fundingRequest.setCoreResponseMessage(fundingResponse.getMessage());
            fundingRequest.setRespondedAt(OffsetDateTime.now());
            fundingRequestRepository.save(fundingRequest);
            updateBatchStatus(batch, BatchStatus.RECHAZADO, resolveFundingRejectionMessage(fundingResponse));
            return;
        }

        applyApprovedFunding(batch, fundingRequest, fundingResponse);
        updateBatchStatus(batch, BatchStatus.FRAGMENTANDO, null);
        publishPaymentLines(batch, parsedBatchFile);
        updateBatchStatus(batch, BatchStatus.PROCESANDO_LINEAS, null);
        LOG.info("Batch funded and payment line events published. batchId={}, totalRecords={}", batchId, batch.getTotalRecords());
    }

    private PaymentBatch findBatch(UUID batchId) {
        return paymentBatchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el lote " + batchId));
    }

    private List<BatchValidationError> validateBatch(PaymentBatch batch, ParsedBatchFile parsedBatchFile) {
        Collection<BatchValidationError> errors = new java.util.ArrayList<>();
        Integer actualLineCount = parsedBatchFile.getLines().size();
        BigDecimal detailTotal = parsedBatchFile.getLines().stream()
                .map(ParsedPaymentLine::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!actualLineCount.equals(parsedBatchFile.getHeaderTotalRecords())) {
            errors.add(error(batch.getBatchId(), "HEADER_COUNT_MISMATCH", "headerTotalRecords",
                    "La cantidad de detalles no coincide con la cabecera."));
        }
        if (!actualLineCount.equals(parsedBatchFile.getFooterTotalRecords())) {
            errors.add(error(batch.getBatchId(), "FOOTER_COUNT_MISMATCH", "footerTotalRecords",
                    "La cantidad de detalles no coincide con el pie de control."));
        }
        if (detailTotal.compareTo(parsedBatchFile.getHeaderControlAmount()) != 0) {
            errors.add(error(batch.getBatchId(), "HEADER_AMOUNT_MISMATCH", "headerControlAmount",
                    "La sumatoria de detalles no coincide con la cabecera."));
        }
        if (detailTotal.compareTo(parsedBatchFile.getFooterControlAmount()) != 0) {
            errors.add(error(batch.getBatchId(), "FOOTER_AMOUNT_MISMATCH", "footerControlAmount",
                    "La sumatoria de detalles no coincide con el pie de control."));
        }
        if (!StringUtils.hasText(parsedBatchFile.getSecurityHash())) {
            errors.add(error(batch.getBatchId(), "MISSING_SECURITY_HASH", "securityHash",
                    "El hash o codigo de seguridad es obligatorio."));
        }
        if (isDuplicateSuccessfulBatch(batch)) {
            errors.add(error(batch.getBatchId(), "DUPLICATE_BATCH", "fileHash",
                    "El lote ya fue recibido previamente y no puede reprocesarse."));
        }

        Set<Integer> seenSequences = new HashSet<>();
        for (ParsedPaymentLine line : parsedBatchFile.getLines()) {
            validateLine(batch.getBatchId(), line, seenSequences, errors);
        }
        return List.copyOf(errors);
    }

    private void validateLine(
            UUID batchId,
            ParsedPaymentLine line,
            Set<Integer> seenSequences,
            Collection<BatchValidationError> errors) {
        String linePrefix = "line[" + line.getSequenceNumber() + "].";
        if (!seenSequences.add(line.getSequenceNumber())) {
            errors.add(error(batchId, "DUPLICATE_SEQUENCE", linePrefix + "sequenceNumber",
                    "La secuencia de linea esta duplicada."));
        }
        if (!VALID_ROUTING_CODES.contains(line.getRoutingCode())) {
            errors.add(error(batchId, "INVALID_ROUTING_CODE", linePrefix + "routingCode",
                    "El routing code no existe en el catalogo local."));
        }
        if (line.getAmount() == null || line.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(error(batchId, "INVALID_AMOUNT", linePrefix + "amount",
                    "El monto de la linea debe ser mayor a cero."));
        }
        if (!StringUtils.hasText(line.getBeneficiaryIdentification())) {
            errors.add(error(batchId, "MISSING_FIELD", linePrefix + "beneficiaryIdentification",
                    "La identificacion del beneficiario es obligatoria."));
        }
        if (!StringUtils.hasText(line.getBeneficiaryName())) {
            errors.add(error(batchId, "MISSING_FIELD", linePrefix + "beneficiaryName",
                    "El nombre del beneficiario es obligatorio."));
        }
        if (!StringUtils.hasText(line.getDestinationAccountNumber())) {
            errors.add(error(batchId, "MISSING_FIELD", linePrefix + "destinationAccountNumber",
                    "La cuenta destino es obligatoria."));
        }
        if (!StringUtils.hasText(line.getNotificationEmail()) || !line.getNotificationEmail().contains("@")) {
            errors.add(error(batchId, "INVALID_NOTIFICATION_EMAIL", linePrefix + "notificationEmail",
                    "El correo de notificacion es obligatorio y debe tener formato basico."));
        }
    }

    private Boolean isDuplicateSuccessfulBatch(PaymentBatch batch) {
        return paymentBatchRepository.existsByFileHashAndStatusInAndReceivedAtAfter(
                batch.getFileHash(),
                DUPLICATE_STATUSES,
                OffsetDateTime.now().minusDays(30));
    }

    private BatchValidationError error(UUID batchId, String code, String fieldName, String message) {
        BatchValidationError validationError = new BatchValidationError();
        validationError.setValidationErrorId(UUID.randomUUID());
        validationError.setBatchId(batchId);
        validationError.setCode(code);
        validationError.setFieldName(fieldName);
        validationError.setMessage(message);
        validationError.setCreatedAt(OffsetDateTime.now());
        return validationError;
    }

    private BatchFundingRequest createFundingRequest(PaymentBatch batch) {
        BatchFundingRequest fundingRequest = new BatchFundingRequest();
        fundingRequest.setFundingRequestId(UUID.randomUUID());
        fundingRequest.setBatchId(batch.getBatchId());
        fundingRequest.setIdempotencyKey("FUNDING-" + batch.getBatchId());
        fundingRequest.setRequestStatus(FundingRequestStatus.SOLICITADO.name());
        fundingRequest.setRequestedAmount(batch.getControlAmount());
        fundingRequest.setCurrency(batch.getCurrency());
        fundingRequest.setRequestedAt(OffsetDateTime.now());
        return fundingRequest;
    }

    private CoreCustomerResponse resolveCompany(PaymentBatch batch) {
        CoreCustomerResponse company = coreCustomerClient.findByIdentification(batch.getCompanyRuc());
        if (company == null || company.getCustomerUuid() == null) {
            throw new CoreCustomerClientException(
                    COMPANY_CUSTOMER_RESOLUTION_FAILED,
                    "Core Customer no devolvio customerUuid para la empresa.");
        }
        if (!StringUtils.hasText(company.getIdentification())
                || !batch.getCompanyRuc().equals(company.getIdentification().trim())) {
            throw new CoreCustomerClientException(
                    COMPANY_CUSTOMER_RESOLUTION_FAILED,
                    "La identificacion devuelta por Core no coincide con el RUC del lote.");
        }
        if (StringUtils.hasText(company.getStatus()) && !isActiveCompanyStatus(company.getStatus())) {
            throw new CoreCustomerClientException(
                    COMPANY_INACTIVE,
                    "La empresa no se encuentra activa en Core.");
        }
        if (Boolean.FALSE.equals(company.getMassPaymentsEnabled())) {
            throw new CoreCustomerClientException(
                    MASS_PAYMENTS_DISABLED,
                    "La empresa no tiene habilitado el servicio de pagos masivos.");
        }
        return company;
    }

    private Boolean isActiveCompanyStatus(String status) {
        return "ACTIVO".equalsIgnoreCase(status) || "ACTIVE".equalsIgnoreCase(status);
    }

    private String formatCompanyRejection(CoreCustomerClientException exception) {
        return exception.getCode() + ": " + exception.getMessage();
    }

    private void validateSourceAccount(PaymentBatch batch, CoreCustomerResponse company) {
        CoreAccountResponse account = coreAccountClient.findByAccountNumber(batch.getSourceAccountNumber());
        if (account == null || !StringUtils.hasText(account.getAccountNumber())) {
            throw new CoreAccountClientException(
                    "SOURCE_ACCOUNT_VALIDATION_FAILED",
                    "Core Account no devolvio informacion de la cuenta matriz.");
        }
        if (!batch.getSourceAccountNumber().equals(account.getAccountNumber().trim())) {
            throw new CoreAccountClientException(
                    "SOURCE_ACCOUNT_VALIDATION_FAILED",
                    "La cuenta devuelta por Core no coincide con la cuenta matriz del lote.");
        }
        if (!StringUtils.hasText(account.getCustomerUuid())
                || !company.getCustomerUuid().toString().equalsIgnoreCase(account.getCustomerUuid().trim())) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_NOT_OWNED_BY_COMPANY,
                    "La cuenta matriz no pertenece a la empresa autenticada.");
        }
        if (StringUtils.hasText(account.getStatus()) && !isActiveAccountStatus(account.getStatus())) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_INACTIVE,
                    "La cuenta matriz no se encuentra activa.");
        }
        if (Boolean.FALSE.equals(account.getMassPaymentMainAccount())) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_NOT_ELIGIBLE,
                    "La cuenta informada no esta habilitada como cuenta matriz de pagos masivos.");
        }
    }

    private Boolean isActiveAccountStatus(String status) {
        return "ACTIVA".equalsIgnoreCase(status) || "ACTIVE".equalsIgnoreCase(status);
    }

    private String formatAccountRejection(CoreAccountClientException exception) {
        return exception.getCode() + ": " + exception.getMessage();
    }

    private CoreFundingResponse requestFunding(
            PaymentBatch batch,
            BatchFundingRequest fundingRequest,
            CoreCustomerResponse company) {
        CoreFundingRequest request = new CoreFundingRequest();
        request.setBatchId(batch.getBatchId());
        request.setCorrelationId(batch.getCorrelationId());
        request.setCompanyRuc(batch.getCompanyRuc());
        request.setCompanyCustomerUuid(company.getCustomerUuid().toString());
        request.setSourceAccountNumber(batch.getSourceAccountNumber());
        request.setMainAccountNumber(batch.getSourceAccountNumber());
        request.setTotalAmount(batch.getControlAmount());
        request.setCommissionAmount(BigDecimal.ZERO);
        request.setCurrency(batch.getCurrency());
        request.setConcept("Fondeo lote de pagos masivos " + batch.getServiceType());
        request.setChannel(coreSwitchChannel);
        request.setAccountingDate(resolveAccountingDate());
        request.setIdempotencyKey(fundingRequest.getIdempotencyKey());
        try {
            return coreBankingClient.requestFunding(request);
        } catch (CoreBankingClientException exception) {
            CoreFundingResponse fallbackResponse = new CoreFundingResponse();
            fallbackResponse.setBatchId(batch.getBatchId());
            fallbackResponse.setStatus(exception.isFunctionalRejection() ? "REJECTED" : "ERROR");
            fallbackResponse.setMessage(exception.getMessage());
            return fallbackResponse;
        }
    }

    private LocalDate resolveAccountingDate() {
        if (StringUtils.hasText(defaultAccountingDate)) {
            try {
                return LocalDate.parse(defaultAccountingDate);
            } catch (DateTimeParseException exception) {
                LOG.warn("Invalid core.switch.default-accounting-date. Falling back to current date. value={}",
                        defaultAccountingDate);
            }
        }
        return LocalDate.now();
    }

    private Boolean isApprovedFundingStatus(String status) {
        return "APPROVED".equalsIgnoreCase(status) || "ACTIVA".equalsIgnoreCase(status);
    }

    private Boolean isTechnicalFundingError(String status) {
        return "ERROR".equalsIgnoreCase(status);
    }

    private String resolveFundingRejectionMessage(CoreFundingResponse fundingResponse) {
        if (StringUtils.hasText(fundingResponse.getMessage())) {
            return fundingResponse.getMessage();
        }
        return "El Core Bancario rechazo el fondeo global.";
    }

    private void applyApprovedFunding(
            PaymentBatch batch,
            BatchFundingRequest fundingRequest,
            CoreFundingResponse fundingResponse) {
        OffsetDateTime now = OffsetDateTime.now();
        fundingRequest.setRequestStatus(FundingRequestStatus.APROBADO.name());
        fundingRequest.setCoreFundingId(fundingResponse.getCoreFundingId());
        fundingRequest.setCoreTransactionId(fundingResponse.getCoreTransactionId());
        fundingRequest.setAccountingDate(fundingResponse.getAccountingDate());
        fundingRequest.setCoreResponseStatus(fundingResponse.getStatus());
        fundingRequest.setCoreResponseMessage(fundingResponse.getMessage());
        fundingRequest.setRespondedAt(now);
        fundingRequestRepository.save(fundingRequest);

        batch.setCoreFundingId(fundingResponse.getCoreFundingId());
        batch.setCoreTransactionId(fundingResponse.getCoreTransactionId());
        batch.setAccountingDate(fundingResponse.getAccountingDate());
        batch.setFundedAt(now);
        updateBatchStatus(batch, BatchStatus.FONDEADO, null);
    }

    private void publishPaymentLines(PaymentBatch batch, ParsedBatchFile parsedBatchFile) {
        for (ParsedPaymentLine parsedLine : parsedBatchFile.getLines()) {
            BatchPaymentLine line = paymentLineMapper.toEntity(batch.getBatchId(), parsedLine);
            paymentLineRepository.save(line);
            UUID eventId = UUID.randomUUID();
            PaymentLineRequestedEvent event = paymentLineMapper.toEvent(batch, line, eventId);
            paymentLineEventPublisher.publish(event);
            line.setEventId(eventId);
            line.setStatus(LineStatus.PUBLICADA.name());
            line.setPublishedAt(OffsetDateTime.now());
            line.setUpdatedAt(OffsetDateTime.now());
            paymentLineRepository.save(line);
        }
    }

    private void updateBatchStatus(PaymentBatch batch, BatchStatus status, String rejectionReason) {
        batch.setStatus(status.name());
        batch.setRejectionReason(rejectionReason);
        batch.setUpdatedAt(OffsetDateTime.now());
        paymentBatchRepository.save(batch);
    }
}
