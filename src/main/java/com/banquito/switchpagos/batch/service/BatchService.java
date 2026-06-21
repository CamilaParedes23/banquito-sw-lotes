package com.banquito.switchpagos.batch.service;

import com.banquito.switchpagos.batch.dto.response.BatchLinesResponse;
import com.banquito.switchpagos.batch.dto.response.BatchHistoryPageResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStateHistoryResponse;
import com.banquito.switchpagos.batch.dto.response.BatchStatusResponse;
import com.banquito.switchpagos.batch.dto.response.BatchValidationErrorsResponse;
import com.banquito.switchpagos.batch.dto.response.UploadBatchResponse;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BatchService {

    UploadBatchResponse uploadBatch(MultipartFile file, String companyRuc, String channel, String receivedBy);

    BatchHistoryPageResponse listBatches(String companyRuc, Integer page, Integer size);

    BatchStatusResponse getBatch(UUID batchId);

    BatchStateHistoryResponse getStateHistory(UUID batchId);

    BatchValidationErrorsResponse getValidationErrors(UUID batchId);

    BatchLinesResponse getLines(UUID batchId);
}
