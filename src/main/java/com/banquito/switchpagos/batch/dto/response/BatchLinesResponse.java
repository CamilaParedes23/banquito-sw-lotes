package com.banquito.switchpagos.batch.dto.response;

import java.util.List;
import java.util.UUID;

public class BatchLinesResponse {

    private UUID batchId;
    private List<BatchLineResponse> lines;

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public List<BatchLineResponse> getLines() {
        return lines;
    }

    public void setLines(List<BatchLineResponse> lines) {
        this.lines = lines;
    }
}
