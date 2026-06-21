package com.banquito.switchpagos.batch.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class CorePaymentReservationResponse {

    private UUID reservationUuid;
    private UUID batchId;
    private String status;
    private BigDecimal reservedAmount;
    private BigDecimal consumedOnUs;
    private BigDecimal consumedOffUs;
    private BigDecimal releasedAmount;
    private String mainAccountNumber;

    public UUID getReservationUuid() {
        return reservationUuid;
    }

    public void setReservationUuid(UUID reservationUuid) {
        this.reservationUuid = reservationUuid;
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

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public BigDecimal getConsumedOnUs() {
        return consumedOnUs;
    }

    public void setConsumedOnUs(BigDecimal consumedOnUs) {
        this.consumedOnUs = consumedOnUs;
    }

    public BigDecimal getConsumedOffUs() {
        return consumedOffUs;
    }

    public void setConsumedOffUs(BigDecimal consumedOffUs) {
        this.consumedOffUs = consumedOffUs;
    }

    public BigDecimal getReleasedAmount() {
        return releasedAmount;
    }

    public void setReleasedAmount(BigDecimal releasedAmount) {
        this.releasedAmount = releasedAmount;
    }

    public String getMainAccountNumber() {
        return mainAccountNumber;
    }

    public void setMainAccountNumber(String mainAccountNumber) {
        this.mainAccountNumber = mainAccountNumber;
    }
}
