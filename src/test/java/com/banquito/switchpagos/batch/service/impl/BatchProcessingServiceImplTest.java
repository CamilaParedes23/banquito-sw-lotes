package com.banquito.switchpagos.batch.service.impl;

import com.banquito.switchpagos.batch.client.CoreBankingClient;
import com.banquito.switchpagos.batch.client.CoreAccountClient;
import com.banquito.switchpagos.batch.client.CoreCustomerClient;
import com.banquito.switchpagos.batch.dto.request.CoreFundingRequest;
import com.banquito.switchpagos.batch.dto.request.ParsedBatchFile;
import com.banquito.switchpagos.batch.dto.response.CoreCustomerResponse;
import com.banquito.switchpagos.batch.dto.response.CoreAccountResponse;
import com.banquito.switchpagos.batch.dto.response.CoreFundingResponse;
import com.banquito.switchpagos.batch.enums.BatchStatus;
import com.banquito.switchpagos.batch.mapper.PaymentLineMapper;
import com.banquito.switchpagos.batch.model.PaymentBatch;
import com.banquito.switchpagos.batch.model.BatchValidationError;
import com.banquito.switchpagos.batch.repository.BatchFundingRequestRepository;
import com.banquito.switchpagos.batch.repository.BatchPaymentLineRepository;
import com.banquito.switchpagos.batch.repository.BatchValidationErrorRepository;
import com.banquito.switchpagos.batch.repository.PaymentBatchRepository;
import com.banquito.switchpagos.batch.service.PaymentLineEventPublisher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BatchProcessingServiceImplTest {

    private PaymentBatchRepository paymentBatchRepository;
    private BatchValidationErrorRepository validationErrorRepository;
    private CoreBankingClient coreBankingClient;
    private CoreCustomerClient coreCustomerClient;
    private CoreAccountClient coreAccountClient;
    private BatchProcessingServiceImpl service;

    @BeforeEach
    void setUp() {
        paymentBatchRepository = mock(PaymentBatchRepository.class);
        validationErrorRepository = mock(BatchValidationErrorRepository.class);
        BatchFundingRequestRepository fundingRequestRepository = mock(BatchFundingRequestRepository.class);
        BatchPaymentLineRepository paymentLineRepository = mock(BatchPaymentLineRepository.class);
        coreBankingClient = mock(CoreBankingClient.class);
        coreCustomerClient = mock(CoreCustomerClient.class);
        coreAccountClient = mock(CoreAccountClient.class);
        PaymentLineMapper paymentLineMapper = mock(PaymentLineMapper.class);
        PaymentLineEventPublisher eventPublisher = mock(PaymentLineEventPublisher.class);
        when(paymentBatchRepository.existsByFileNameAndFileHashAndStatusInAndReceivedAtAfter(
                any(), any(), any(), any())).thenReturn(false);
        when(paymentBatchRepository.existsByFileHashAndStatusInAndReceivedAtAfter(
                any(), any(), any())).thenReturn(false);
        service = new BatchProcessingServiceImpl(
                paymentBatchRepository,
                validationErrorRepository,
                fundingRequestRepository,
                paymentLineRepository,
                coreBankingClient,
                coreCustomerClient,
                coreAccountClient,
                paymentLineMapper,
                eventPublisher,
                "2026-06-05",
                "SWITCH_API");
    }

    @Test
    void shouldUseCustomerUuidResolvedFromBatchCompanyRuc() {
        PaymentBatch batch = batch();
        ParsedBatchFile parsedBatchFile = validEmptyBatch();
        UUID customerUuid = UUID.randomUUID();
        CoreCustomerResponse company = customer(customerUuid, "ACTIVO", true);
        CoreFundingResponse fundingResponse = new CoreFundingResponse();
        fundingResponse.setCoreFundingId(UUID.randomUUID().toString());
        fundingResponse.setStatus("ACTIVA");
        fundingResponse.setAccountingDate(LocalDate.of(2026, 6, 5));
        when(paymentBatchRepository.findById(batch.getBatchId())).thenReturn(Optional.of(batch));
        when(coreCustomerClient.findByIdentification("1792103456001")).thenReturn(company);
        when(coreAccountClient.findByAccountNumber("0010000010599"))
                .thenReturn(account("0010000010599", customerUuid.toString(), "ACTIVA", true, "OPERATIVA"));
        when(coreBankingClient.requestFunding(any(CoreFundingRequest.class))).thenReturn(fundingResponse);

        service.processBatch(batch.getBatchId(), parsedBatchFile);

        ArgumentCaptor<CoreFundingRequest> requestCaptor = ArgumentCaptor.forClass(CoreFundingRequest.class);
        verify(coreBankingClient).requestFunding(requestCaptor.capture());
        assertEquals(customerUuid.toString(), requestCaptor.getValue().getCompanyCustomerUuid());
        assertEquals("1792103456001", requestCaptor.getValue().getCompanyRuc());
        assertEquals(BatchStatus.PROCESANDO_LINEAS.name(), batch.getStatus());
    }

    @Test
    void shouldRejectBatchWhenMassPaymentsAreDisabled() {
        PaymentBatch batch = batch();
        when(paymentBatchRepository.findById(batch.getBatchId())).thenReturn(Optional.of(batch));
        when(coreCustomerClient.findByIdentification("1792103456001"))
                .thenReturn(customer(UUID.randomUUID(), "ACTIVO", false));

        service.processBatch(batch.getBatchId(), validEmptyBatch());

        verify(coreBankingClient, never()).requestFunding(any(CoreFundingRequest.class));
        assertEquals(BatchStatus.RECHAZADO.name(), batch.getStatus());
        assertTrue(batch.getRejectionReason().startsWith("MASS_PAYMENTS_DISABLED:"));
    }

    @Test
    void shouldRejectBatchWhenSourceAccountBelongsToAnotherCustomer() {
        PaymentBatch batch = batch();
        UUID customerUuid = UUID.randomUUID();
        when(paymentBatchRepository.findById(batch.getBatchId())).thenReturn(Optional.of(batch));
        when(coreCustomerClient.findByIdentification("1792103456001"))
                .thenReturn(customer(customerUuid, "ACTIVO", true));
        when(coreAccountClient.findByAccountNumber("0010000010599"))
                .thenReturn(account("0010000010599", UUID.randomUUID().toString(), "ACTIVA", true, "MASS_PAYMENTS"));

        service.processBatch(batch.getBatchId(), validEmptyBatch());

        verify(coreBankingClient, never()).requestFunding(any(CoreFundingRequest.class));
        assertEquals(BatchStatus.RECHAZADO.name(), batch.getStatus());
        assertTrue(batch.getRejectionReason().startsWith("SOURCE_ACCOUNT_NOT_OWNED_BY_COMPANY:"));
    }

    @Test
    void shouldRejectDuplicateBatchWhenHashAlreadyClosed() {
        PaymentBatch batch = batch();
        when(paymentBatchRepository.findById(batch.getBatchId())).thenReturn(Optional.of(batch));
        when(paymentBatchRepository.existsByFileHashAndStatusInAndReceivedAtAfter(
                any(), any(), any())).thenReturn(true);

        service.processBatch(batch.getBatchId(), validEmptyBatch());

        ArgumentCaptor<List<BatchValidationError>> errorsCaptor = ArgumentCaptor.forClass(List.class);
        verify(validationErrorRepository).saveAll(errorsCaptor.capture());
        assertEquals("DUPLICATE_BATCH", errorsCaptor.getValue().getFirst().getCode());
        verify(coreBankingClient, never()).requestFunding(any(CoreFundingRequest.class));
        assertEquals(BatchStatus.RECHAZADO.name(), batch.getStatus());
    }

    private PaymentBatch batch() {
        OffsetDateTime now = OffsetDateTime.now();
        PaymentBatch batch = new PaymentBatch();
        batch.setBatchId(UUID.randomUUID());
        batch.setCorrelationId(UUID.randomUUID());
        batch.setCompanyRuc("1792103456001");
        batch.setSourceAccountNumber("0010000010599");
        batch.setServiceType("NOMINA");
        batch.setFileName("batch.csv");
        batch.setFileHash("hash");
        batch.setChannel("PORTAL_WEB");
        batch.setTotalRecords(0);
        batch.setControlAmount(BigDecimal.TEN);
        batch.setCurrency("USD");
        batch.setStatus(BatchStatus.RECIBIDO.name());
        batch.setReceivedAt(now);
        batch.setCreatedAt(now);
        batch.setUpdatedAt(now);
        return batch;
    }

    private ParsedBatchFile validEmptyBatch() {
        ParsedBatchFile parsed = new ParsedBatchFile();
        parsed.setCompanyRuc("1792103456001");
        parsed.setServiceType("NOMINA");
        parsed.setSourceAccountNumber("0010000010599");
        parsed.setHeaderTotalRecords(0);
        parsed.setHeaderControlAmount(BigDecimal.ZERO);
        parsed.setFooterTotalRecords(0);
        parsed.setFooterControlAmount(BigDecimal.ZERO);
        parsed.setSecurityHash("hash");
        parsed.setLines(List.of());
        return parsed;
    }

    private CoreCustomerResponse customer(UUID customerUuid, String status, Boolean massPaymentsEnabled) {
        CoreCustomerResponse response = new CoreCustomerResponse();
        response.setCustomerUuid(customerUuid);
        response.setIdentification("1792103456001");
        response.setStatus(status);
        response.setMassPaymentsEnabled(massPaymentsEnabled);
        return response;
    }

    private CoreAccountResponse account(
            String accountNumber,
            String customerUuid,
            String status,
            Boolean massPaymentMainAccount,
            String accountPurpose) {
        CoreAccountResponse response = new CoreAccountResponse();
        response.setAccountNumber(accountNumber);
        response.setCustomerUuid(customerUuid);
        response.setStatus(status);
        response.setMassPaymentMainAccount(massPaymentMainAccount);
        response.setAccountPurpose(accountPurpose);
        return response;
    }
}
