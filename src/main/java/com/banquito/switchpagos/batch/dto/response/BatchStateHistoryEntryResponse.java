package com.banquito.switchpagos.batch.dto.response;

import java.time.OffsetDateTime;

public class BatchStateHistoryEntryResponse {

    private String previousStatus;
    private String newStatus;
    private String reason;
    private String changedBy;
    private OffsetDateTime changedAt;

    public String getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public OffsetDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(OffsetDateTime changedAt) { this.changedAt = changedAt; }
}
