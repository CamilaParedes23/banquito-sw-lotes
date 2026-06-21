package com.banquito.switchpagos.batch.dto.response;

import java.math.BigDecimal;

public class CoreCompanyAccountValidationResponse {

    private Boolean valid;
    private String code;
    private String message;
    private String companyCustomerUuid;
    private String mainAccountNumber;
    private String accountUuid;
    private String accountStatus;
    private String accountPurpose;
    private Boolean massPaymentMainAccount;
    private BigDecimal accountingBalance;
    private BigDecimal availableBalance;
    private BigDecimal requestedAmount;
    private Boolean amountCovered;
    private Boolean overdraftAllowed;
    private BigDecimal overdraftLimit;
    private BigDecimal overdraftAvailable;

    public Boolean getValid() { return valid; }
    public void setValid(Boolean valid) { this.valid = valid; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCompanyCustomerUuid() { return companyCustomerUuid; }
    public void setCompanyCustomerUuid(String companyCustomerUuid) { this.companyCustomerUuid = companyCustomerUuid; }
    public String getMainAccountNumber() { return mainAccountNumber; }
    public void setMainAccountNumber(String mainAccountNumber) { this.mainAccountNumber = mainAccountNumber; }
    public String getAccountUuid() { return accountUuid; }
    public void setAccountUuid(String accountUuid) { this.accountUuid = accountUuid; }
    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public String getAccountPurpose() { return accountPurpose; }
    public void setAccountPurpose(String accountPurpose) { this.accountPurpose = accountPurpose; }
    public Boolean getMassPaymentMainAccount() { return massPaymentMainAccount; }
    public void setMassPaymentMainAccount(Boolean massPaymentMainAccount) { this.massPaymentMainAccount = massPaymentMainAccount; }
    public BigDecimal getAccountingBalance() { return accountingBalance; }
    public void setAccountingBalance(BigDecimal accountingBalance) { this.accountingBalance = accountingBalance; }
    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    public Boolean getAmountCovered() { return amountCovered; }
    public void setAmountCovered(Boolean amountCovered) { this.amountCovered = amountCovered; }
    public Boolean getOverdraftAllowed() { return overdraftAllowed; }
    public void setOverdraftAllowed(Boolean overdraftAllowed) { this.overdraftAllowed = overdraftAllowed; }
    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(BigDecimal overdraftLimit) { this.overdraftLimit = overdraftLimit; }
    public BigDecimal getOverdraftAvailable() { return overdraftAvailable; }
    public void setOverdraftAvailable(BigDecimal overdraftAvailable) { this.overdraftAvailable = overdraftAvailable; }
}
