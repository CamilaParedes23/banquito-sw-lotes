package com.banquito.switchpagos.batch.dto.response;

import java.util.UUID;

public class CoreCustomerResponse {

    private UUID customerUuid;
    private String customerType;
    private String identificationType;
    private String identification;
    private String displayName;
    private String status;
    private Boolean massPaymentsEnabled;

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getMassPaymentsEnabled() {
        return massPaymentsEnabled;
    }

    public void setMassPaymentsEnabled(Boolean massPaymentsEnabled) {
        this.massPaymentsEnabled = massPaymentsEnabled;
    }
}
