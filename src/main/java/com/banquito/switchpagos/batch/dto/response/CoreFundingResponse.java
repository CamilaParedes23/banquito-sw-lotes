package com.banquito.switchpagos.batch.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CoreFundingResponse {

    private String coreFundingId;
    private UUID batchId;
    private String status;
    private BigDecimal fundedAmount;
    private LocalDate accountingDate;
    private String coreTransactionId;
    private String message;

    public String getCoreFundingId() {
        return coreFundingId;
    }

    public void setCoreFundingId(String coreFundingId) {
        this.coreFundingId = coreFundingId;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFundedAmount() {
        return fundedAmount;
    }

    public void setFundedAmount(BigDecimal fundedAmount) {
        this.fundedAmount = fundedAmount;
    }

    public LocalDate getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(LocalDate accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getCoreTransactionId() {
        return coreTransactionId;
    }

    public void setCoreTransactionId(String coreTransactionId) {
        this.coreTransactionId = coreTransactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
