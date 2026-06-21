package com.banquito.switchpagos.batch.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BatchStateHistoryResponse {

    private UUID batchId;
    private List<BatchStateHistoryEntryResponse> history = new ArrayList<>();

    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public List<BatchStateHistoryEntryResponse> getHistory() { return history; }
    public void setHistory(List<BatchStateHistoryEntryResponse> history) { this.history = history; }
}
