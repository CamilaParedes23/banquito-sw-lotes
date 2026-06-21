package com.banquito.switchpagos.batch.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CorePaymentReservationRequest {

    private UUID batchId;
    private UUID correlationId;
    private String companyCustomerUuid;
    private String mainAccountNumber;
    private BigDecimal totalAmount;
    private BigDecimal commissionAmount;
    private String channel;
    private LocalDate accountingDate;

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

    public String getCompanyCustomerUuid() {
        return companyCustomerUuid;
    }

    public void setCompanyCustomerUuid(String companyCustomerUuid) {
        this.companyCustomerUuid = companyCustomerUuid;
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
}
