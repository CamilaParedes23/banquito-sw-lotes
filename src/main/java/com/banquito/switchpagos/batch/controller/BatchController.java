package com.banquito.switchpagos.batch.controller;

import com.banquito.switchpagos.batch.dto.response.BatchLinesResponse;
import com.banquito.switchpagos.batch.dto.response.BatchHistoryPageResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStateHistoryResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStatusResponse;
import com.banquito.switchpagos.batch.dto.response.BatchValidationErrorsResponse;
import com.banquito.switchpagos.batch.dto.response.UploadBatchResponse;
import com.banquito.switchpagos.batch.exception.BadRequestException;
import com.banquito.switchpagos.batch.service.BatchService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {

    private static final Logger LOG = LoggerFactory.getLogger(BatchController.class);

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadBatchResponse> uploadBatch(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "companyRuc", required = false) String companyRuc,
            @RequestParam(value = "companyCustomerUuid", required = false) String companyCustomerUuid,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "receivedBy", required = false) String receivedBy) {
        LOG.info(
                "Batch upload identity fields. companyRucPresent={}, companyCustomerUuidPresent={}, channelPresent={}, receivedByPresent={}",
                StringUtils.hasText(companyRuc),
                StringUtils.hasText(companyCustomerUuid),
                StringUtils.hasText(channel),
                StringUtils.hasText(receivedBy));
        if (!StringUtils.hasText(companyRuc)) {
            throw new BadRequestException(
                    "COMPANY_RUC_REQUIRED",
                    "El RUC de la empresa autenticada es obligatorio para cargar el lote.");
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(batchService.uploadBatch(file, companyRuc, companyCustomerUuid, channel, receivedBy));
    }

    @GetMapping
    public ResponseEntity<BatchHistoryPageResponse> listBatches(
            @RequestParam(value = "companyRuc", required = false) String companyRuc,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(batchService.listBatches(companyRuc, page, size));
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<BatchStatusResponse> getBatch(@PathVariable UUID batchId) {
        return ResponseEntity.ok(batchService.getBatch(batchId));
    }

    @GetMapping("/{batchId}/state-history")
    public ResponseEntity<BatchStateHistoryResponse> getStateHistory(@PathVariable UUID batchId) {
        return ResponseEntity.ok(batchService.getStateHistory(batchId));
    }

    @GetMapping("/{batchId}/validation-errors")
    public ResponseEntity<BatchValidationErrorsResponse> getValidationErrors(@PathVariable UUID batchId) {
        return ResponseEntity.ok(batchService.getValidationErrors(batchId));
    }

    @GetMapping("/{batchId}/lines")
    public ResponseEntity<BatchLinesResponse> getLines(@PathVariable UUID batchId) {
        return ResponseEntity.ok(batchService.getLines(batchId));
    }
}
