package com.banquito.switchpagos.batch.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BatchHistoryItemResponse {

    private UUID batchId;
    private String fileName;
    private OffsetDateTime receivedAt;
    private String status;
    private Integer totalRecords;
    private BigDecimal controlAmount;
    private String companyRuc;

    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public OffsetDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(OffsetDateTime receivedAt) { this.receivedAt = receivedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getTotalRecords() { return totalRecords; }
    public void setTotalRecords(Integer totalRecords) { this.totalRecords = totalRecords; }
    public BigDecimal getControlAmount() { return controlAmount; }
    public void setControlAmount(BigDecimal controlAmount) { this.controlAmount = controlAmount; }
    public String getCompanyRuc() { return companyRuc; }
    public void setCompanyRuc(String companyRuc) { this.companyRuc = companyRuc; }
}
