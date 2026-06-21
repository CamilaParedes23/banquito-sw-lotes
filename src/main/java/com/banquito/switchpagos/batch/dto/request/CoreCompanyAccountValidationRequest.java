package com.banquito.switchpagos.batch.dto.request;

import java.math.BigDecimal;

public class CoreCompanyAccountValidationRequest {

    private String companyCustomerUuid;
    private String mainAccountNumber;
    private BigDecimal amount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
