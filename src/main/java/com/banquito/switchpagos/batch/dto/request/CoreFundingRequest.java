package com.banquito.switchpagos.batch.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CoreFundingRequest {

    private UUID batchId;
    private UUID correlationId;
    private String companyRuc;
    private String companyCustomerUuid;
    private String sourceAccountNumber;
    private String mainAccountNumber;
    private BigDecimal totalAmount;
    private BigDecimal commissionAmount;
    private String currency;
    private String concept;
    private String channel;
    private LocalDate accountingDate;
    private String idempotencyKey;

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public String getCompanyRuc() {
        return companyRuc;
    }

    public void setCompanyRuc(String companyRuc) {
        this.companyRuc = companyRuc;
    }

    public String getCompanyCustomerUuid() {
        return companyCustomerUuid;
    }

    public void setCompanyCustomerUuid(String companyCustomerUuid) {
        this.companyCustomerUuid = companyCustomerUuid;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getMainAccountNumber() {
        return mainAccountNumber;
    }

    public void setMainAccountNumber(String mainAccountNumber) {
        this.mainAccountNumber = mainAccountNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public LocalDate getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(LocalDate accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
}
