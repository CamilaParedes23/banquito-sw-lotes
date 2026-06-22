package com.banquito.switchpagos.batch.service.impl;

import com.banquito.switchpagos.batch.dto.request.ParsedBatchFile;
import com.banquito.switchpagos.batch.dto.response.BatchHistoryItemResponse;
import com.banquito.switchpagos.batch.dto.response.BatchHistoryPageResponse;
import com.banquito.switchpagos.batch.dto.response.BatchLinesResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStateHistoryEntryResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStateHistoryResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStatusResponse;
import com.banquito.switchpagos.batch.dto.response.BatchValidationErrorsResponse;
import com.banquito.switchpagos.batch.dto.response.UploadBatchResponse;
import com.banquito.switchpagos.batch.enums.BatchStatus;
import com.banquito.switchpagos.batch.exception.BadRequestException;
import com.banquito.switchpagos.batch.exception.ResourceNotFoundException;
import com.banquito.switchpagos.batch.mapper.BatchFileParser;
import com.banquito.switchpagos.batch.mapper.BatchMapper;
import com.banquito.switchpagos.batch.mapper.PaymentLineMapper;
import com.banquito.switchpagos.batch.mapper.ValidationErrorMapper;
import com.banquito.switchpagos.batch.model.PaymentBatch;
import com.banquito.switchpagos.batch.model.UploadedFile;
import com.banquito.switchpagos.batch.repository.BatchPaymentLineRepository;
import com.banquito.switchpagos.batch.repository.BatchValidationErrorRepository;
import com.banquito.switchpagos.batch.repository.PaymentBatchRepository;
import com.banquito.switchpagos.batch.repository.UploadedFileRepository;
import com.banquito.switchpagos.batch.service.BatchProcessingService;
import com.banquito.switchpagos.batch.service.BatchService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BatchServiceImpl implements BatchService {

    private static final String DEFAULT_CHANNEL = "WEB";
    private static final String DEFAULT_CURRENCY = "USD";

    private final BatchFileParser batchFileParser;
    private final PaymentBatchRepository paymentBatchRepository;
    private final UploadedFileRepository uploadedFileRepository;
    private final BatchValidationErrorRepository validationErrorRepository;
    private final BatchPaymentLineRepository paymentLineRepository;
    private final BatchProcessingService batchProcessingService;
    private final BatchMapper batchMapper;
    private final ValidationErrorMapper validationErrorMapper;
    private final PaymentLineMapper paymentLineMapper;

    public BatchServiceImpl(
            BatchFileParser batchFileParser,
            PaymentBatchRepository paymentBatchRepository,
            UploadedFileRepository uploadedFileRepository,
            BatchValidationErrorRepository validationErrorRepository,
            BatchPaymentLineRepository paymentLineRepository,
            BatchProcessingService batchProcessingService,
            BatchMapper batchMapper,
            ValidationErrorMapper validationErrorMapper,
            PaymentLineMapper paymentLineMapper) {
        this.batchFileParser = batchFileParser;
        this.paymentBatchRepository = paymentBatchRepository;
        this.uploadedFileRepository = uploadedFileRepository;
        this.validationErrorRepository = validationErrorRepository;
        this.paymentLineRepository = paymentLineRepository;
        this.batchProcessingService = batchProcessingService;
        this.batchMapper = batchMapper;
        this.validationErrorMapper = validationErrorMapper;
        this.paymentLineMapper = paymentLineMapper;
    }

    @Override
    @Transactional
    public UploadBatchResponse uploadBatch(
            MultipartFile file,
            String companyRuc,
            String companyCustomerUuid,
            String channel,
            String receivedBy) {
        validateFile(file);
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String sanitizedFileName = sanitizeFileName(originalFileName);
        String fileContent = readFileContent(file);
        ParsedBatchFile parsedBatchFile = batchFileParser.parse(fileContent);
        validateAuthenticatedCompanyRuc(companyRuc, parsedBatchFile.getCompanyRuc());
        parsedBatchFile.setCompanyCustomerUuid(trimToNull(companyCustomerUuid));
        OffsetDateTime now = OffsetDateTime.now();

        PaymentBatch batch = new PaymentBatch();
        batch.setBatchId(UUID.randomUUID());
        batch.setCorrelationId(UUID.randomUUID());
        batch.setCompanyRuc(parsedBatchFile.getCompanyRuc());
        batch.setSourceAccountNumber(parsedBatchFile.getSourceAccountNumber());
        batch.setServiceType(parsedBatchFile.getServiceType());
        batch.setFileName(sanitizedFileName);
        batch.setFileHash(parsedBatchFile.getSecurityHash());
        batch.setChannel(normalizeChannel(channel));
        batch.setReceivedBy(trimToNull(receivedBy));
        batch.setTotalRecords(parsedBatchFile.getHeaderTotalRecords());
        batch.setControlAmount(parsedBatchFile.getHeaderControlAmount());
        batch.setCurrency(DEFAULT_CURRENCY);
        batch.setStatus(BatchStatus.RECIBIDO.name());
        batch.setReceivedAt(now);
        batch.setCreatedAt(now);
        batch.setUpdatedAt(now);
        paymentBatchRepository.save(batch);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setUploadedFileId(UUID.randomUUID());
        uploadedFile.setBatchId(batch.getBatchId());
        uploadedFile.setOriginalFileName(originalFileName);
        uploadedFile.setSanitizedFileName(sanitizedFileName);
        uploadedFile.setFileExtension(getExtension(sanitizedFileName));
        uploadedFile.setFileHash(parsedBatchFile.getSecurityHash());
        uploadedFile.setContentType(file.getContentType());
        uploadedFile.setFileSizeBytes(file.getSize());
        uploadedFile.setStoredReference("db-metadata-only:" + batch.getBatchId());
        uploadedFile.setUploadedAt(now);
        uploadedFileRepository.save(uploadedFile);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                batchProcessingService.processBatch(batch.getBatchId(), parsedBatchFile);
            }
        });
        return batchMapper.toUploadResponse(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public BatchHistoryPageResponse listBatches(String companyRuc, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(normalizePage(page), normalizeSize(size));
        Page<PaymentBatch> batches = StringUtils.hasText(companyRuc)
                ? paymentBatchRepository.findByCompanyRucOrderByReceivedAtDesc(companyRuc.trim(), pageable)
                : paymentBatchRepository.findAllByOrderByReceivedAtDesc(pageable);
        BatchHistoryPageResponse response = new BatchHistoryPageResponse();
        response.setContent(batches.getContent().stream().map(this::toHistoryItem).toList());
        response.setTotalElements(batches.getTotalElements());
        response.setTotalPages(batches.getTotalPages());
        response.setCurrentPage(batches.getNumber());
        response.setPageSize(batches.getSize());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BatchStatusResponse getBatch(UUID batchId) {
        return batchMapper.toStatusResponse(findBatch(batchId));
    }

    @Override
    @Transactional(readOnly = true)
    public BatchStateHistoryResponse getStateHistory(UUID batchId) {
        PaymentBatch batch = findBatch(batchId);
        BatchStateHistoryResponse response = new BatchStateHistoryResponse();
        response.setBatchId(batchId);
        List<BatchStateHistoryEntryResponse> history = new ArrayList<>();
        addHistoryEntry(history, null, BatchStatus.RECIBIDO.name(),
                "Archivo recibido desde " + batch.getChannel(), "BATCH_SERVICE", batch.getReceivedAt());
        addHistoryEntry(history, BatchStatus.RECIBIDO.name(), BatchStatus.VALIDADO.name(),
                "Validacion profunda ejecutada por batch-service.", "BATCH_SERVICE", batch.getValidatedAt());
        addHistoryEntry(history, BatchStatus.VALIDADO.name(), BatchStatus.FONDEADO.name(),
                "Reserva/fondeo global aprobado por Core.", "BATCH_SERVICE", batch.getFundedAt());
        if (!history.isEmpty()) {
            BatchStateHistoryEntryResponse last = history.get(history.size() - 1);
            if (!batch.getStatus().equals(last.getNewStatus())) {
                addHistoryEntry(history, last.getNewStatus(), batch.getStatus(),
                        resolveCurrentStateReason(batch), "BATCH_SERVICE", batch.getUpdatedAt());
            }
        }
        response.setHistory(history);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BatchValidationErrorsResponse getValidationErrors(UUID batchId) {
        findBatch(batchId);
        BatchValidationErrorsResponse response = new BatchValidationErrorsResponse();
        response.setBatchId(batchId);
        response.setErrors(validationErrorRepository.findByBatchIdOrderByCreatedAtAsc(batchId)
                .stream()
                .map(validationErrorMapper::toResponse)
                .toList());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BatchLinesResponse getLines(UUID batchId) {
        findBatch(batchId);
        BatchLinesResponse response = new BatchLinesResponse();
        response.setBatchId(batchId);
        response.setLines(paymentLineRepository.findByBatchIdOrderBySequenceNumberAsc(batchId)
                .stream()
                .map(paymentLineMapper::toResponse)
                .toList());
        return response;
    }

    private PaymentBatch findBatch(UUID batchId) {
        return paymentBatchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el lote " + batchId));
    }

    private BatchHistoryItemResponse toHistoryItem(PaymentBatch batch) {
        BatchHistoryItemResponse response = new BatchHistoryItemResponse();
        response.setBatchId(batch.getBatchId());
        response.setFileName(batch.getFileName());
        response.setReceivedAt(batch.getReceivedAt());
        response.setStatus(batch.getStatus());
        response.setTotalRecords(batch.getTotalRecords());
        response.setControlAmount(batch.getControlAmount());
        response.setCompanyRuc(batch.getCompanyRuc());
        return response;
    }

    private Integer normalizePage(Integer page) {
        if (page == null || page < 0) {
            return 0;
        }
        return page;
    }

    private Integer normalizeSize(Integer size) {
        if (size == null) {
            return 10;
        }
        if (size < 1) {
            return 10;
        }
        return Math.min(size, 100);
    }

    private void addHistoryEntry(List<BatchStateHistoryEntryResponse> history,
                                 String previousStatus,
                                 String newStatus,
                                 String reason,
                                 String changedBy,
                                 OffsetDateTime changedAt) {
        if (changedAt == null) {
            return;
        }
        BatchStateHistoryEntryResponse entry = new BatchStateHistoryEntryResponse();
        entry.setPreviousStatus(previousStatus);
        entry.setNewStatus(newStatus);
        entry.setReason(reason);
        entry.setChangedBy(changedBy);
        entry.setChangedAt(changedAt);
        history.add(entry);
    }

    private String resolveCurrentStateReason(PaymentBatch batch) {
        if (StringUtils.hasText(batch.getRejectionReason())) {
            return batch.getRejectionReason();
        }
        return "Estado actual del lote.";
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo de lote es obligatorio.");
        }
        String sanitizedFileName = sanitizeFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        String extension = getExtension(sanitizedFileName);
        if (!"csv".equalsIgnoreCase(extension)) {
            throw new BadRequestException("El archivo debe tener extension .csv.");
        }
    }

    private void validateAuthenticatedCompanyRuc(String authenticatedCompanyRuc, String fileCompanyRuc) {
        if (!StringUtils.hasText(authenticatedCompanyRuc)) {
            return;
        }
        String expectedCompanyRuc = authenticatedCompanyRuc.trim();
        String actualCompanyRuc = StringUtils.hasText(fileCompanyRuc) ? fileCompanyRuc.trim() : "";
        if (!expectedCompanyRuc.equals(actualCompanyRuc)) {
            throw new BadRequestException(
                    "COMPANY_RUC_MISMATCH",
                    "El RUC del archivo no coincide con la empresa autenticada.");
        }
    }

    private String readFileContent(MultipartFile file) {
        try {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new BadRequestException("No fue posible leer el archivo cargado.", exception);
        }
    }

    private String sanitizeFileName(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new BadRequestException("El nombre del archivo es obligatorio.");
        }
        String baseName = fileName.replace("\\", "/");
        int separatorIndex = baseName.lastIndexOf('/');
        if (separatorIndex >= 0) {
            baseName = baseName.substring(separatorIndex + 1);
        }
        String sanitized = baseName.replaceAll("[^A-Za-z0-9._-]", "_");
        if (!StringUtils.hasText(sanitized) || ".".equals(sanitized) || "..".equals(sanitized)) {
            throw new BadRequestException("El nombre del archivo no es valido.");
        }
        return sanitized;
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

    private String normalizeChannel(String channel) {
        if (!StringUtils.hasText(channel)) {
            return DEFAULT_CHANNEL;
        }
        return channel.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
