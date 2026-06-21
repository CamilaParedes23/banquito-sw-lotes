package com.banquito.switchpagos.batch.dto.response;

import java.math.BigDecimal;

public class CoreAccountResponse {

    private String accountUuid;
    private String accountNumber;
    private String customerUuid;
    private String identification;
    private String holderName;
    private String branchCode;
    private String subtypeCode;
    private String status;
    private BigDecimal accountingBalance;
    private BigDecimal availableBalance;
    private BigDecimal withheldAmount;
    private Boolean favoritePaymentAccount;
    private Boolean massPaymentMainAccount;
    private String accountPurpose;
    private String operationalAlias;

    public String getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(String accountUuid) {
        this.accountUuid = accountUuid;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getSubtypeCode() {
        return subtypeCode;
    }

    public void setSubtypeCode(String subtypeCode) {
        this.subtypeCode = subtypeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAccountingBalance() {
        return accountingBalance;
    }

    public void setAccountingBalance(BigDecimal accountingBalance) {
        this.accountingBalance = accountingBalance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getWithheldAmount() {
        return withheldAmount;
    }

    public void setWithheldAmount(BigDecimal withheldAmount) {
        this.withheldAmount = withheldAmount;
    }

    public Boolean getFavoritePaymentAccount() {
        return favoritePaymentAccount;
    }

    public void setFavoritePaymentAccount(Boolean favoritePaymentAccount) {
        this.favoritePaymentAccount = favoritePaymentAccount;
    }

    public Boolean getMassPaymentMainAccount() {
        return massPaymentMainAccount;
    }

    public void setMassPaymentMainAccount(Boolean massPaymentMainAccount) {
        this.massPaymentMainAccount = massPaymentMainAccount;
    }

    public String getAccountPurpose() {
        return accountPurpose;
    }

    public void setAccountPurpose(String accountPurpose) {
        this.accountPurpose = accountPurpose;
    }

    public String getOperationalAlias() {
        return operationalAlias;
    }

    public void setOperationalAlias(String operationalAlias) {
        this.operationalAlias = operationalAlias;
    }
}
