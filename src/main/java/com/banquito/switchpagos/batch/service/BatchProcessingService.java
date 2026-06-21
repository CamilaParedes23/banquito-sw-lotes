package com.banquito.switchpagos.batch.service;

import com.banquito.switchpagos.batch.dto.request.ParsedBatchFile;
import java.util.UUID;

public interface BatchProcessingService {

    void processBatch(UUID batchId, ParsedBatchFile parsedBatchFile);
}
