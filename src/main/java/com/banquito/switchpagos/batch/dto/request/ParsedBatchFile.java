package com.banquito.switchpagos.batch.dto.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParsedBatchFile {

    private String companyRuc;
    private String serviceType;
    private OffsetDateTime generatedAt;
    private String sourceAccountNumber;
    private Integer headerTotalRecords;
    private BigDecimal headerControlAmount;
    private String securityHash;
    private Integer footerTotalRecords;
    private BigDecimal footerControlAmount;
    private List<ParsedPaymentLine> lines = new ArrayList<>();

    public String getCompanyRuc() {
        return companyRuc;
    }

    public void setCompanyRuc(String companyRuc) {
        this.companyRuc = companyRuc;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(OffsetDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public Integer getHeaderTotalRecords() {
        return headerTotalRecords;
    }

    public void setHeaderTotalRecords(Integer headerTotalRecords) {
        this.headerTotalRecords = headerTotalRecords;
    }

    public BigDecimal getHeaderControlAmount() {
        return headerControlAmount;
    }

    public void setHeaderControlAmount(BigDecimal headerControlAmount) {
        this.headerControlAmount = headerControlAmount;
    }

    public String getSecurityHash() {
        return securityHash;
    }

    public void setSecurityHash(String securityHash) {
        this.securityHash = securityHash;
    }

    public Integer getFooterTotalRecords() {
        return footerTotalRecords;
    }

    public void setFooterTotalRecords(Integer footerTotalRecords) {
        this.footerTotalRecords = footerTotalRecords;
    }

    public BigDecimal getFooterControlAmount() {
        return footerControlAmount;
    }

    public void setFooterControlAmount(BigDecimal footerControlAmount) {
        this.footerControlAmount = footerControlAmount;
    }

    public List<ParsedPaymentLine> getLines() {
        return lines;
    }

    public void setLines(List<ParsedPaymentLine> lines) {
        this.lines = lines;
    }
}
