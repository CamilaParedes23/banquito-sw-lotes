package com.banquito.switchpagos.batch.mapper;

import com.banquito.switchpagos.batch.dto.request.ParsedBatchFile;
import com.banquito.switchpagos.batch.dto.request.ParsedPaymentLine;
import com.banquito.switchpagos.batch.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BatchFileParser {

    public ParsedBatchFile parse(String content) {
        List<String> rawLines = content.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
        if (rawLines.size() < 3) {
            throw new BadRequestException("El archivo debe contener cabecera, detalle y pie.");
        }

        ParsedBatchFile parsedBatchFile = new ParsedBatchFile();
        List<ParsedPaymentLine> paymentLines = new ArrayList<>();
        Boolean headerFound = Boolean.FALSE;
        Boolean footerFound = Boolean.FALSE;

        for (String rawLine : rawLines) {
            String[] columns = split(rawLine);
            String recordType = columns[0].trim();
            if ("H".equals(recordType)) {
                parseHeader(columns, parsedBatchFile);
                headerFound = Boolean.TRUE;
            } else if ("D".equals(recordType)) {
                paymentLines.add(parseDetail(columns));
            } else if ("T".equals(recordType)) {
                parseFooter(columns, parsedBatchFile);
                footerFound = Boolean.TRUE;
            } else {
                throw new BadRequestException("Tipo de registro no soportado: " + recordType);
            }
        }

        if (!headerFound) {
            throw new BadRequestException("El archivo no contiene cabecera.");
        }
        if (paymentLines.isEmpty()) {
            throw new BadRequestException("El archivo no contiene lineas de detalle.");
        }
        if (!footerFound) {
            throw new BadRequestException("El archivo no contiene pie de control.");
        }

        parsedBatchFile.setLines(paymentLines);
        return parsedBatchFile;
    }

    private void parseHeader(String[] columns, ParsedBatchFile parsedBatchFile) {
        if (columns.length != 7) {
            throw new BadRequestException("La cabecera debe tener 7 columnas.");
        }
        parsedBatchFile.setCompanyRuc(required(columns[1], "companyRuc"));
        parsedBatchFile.setServiceType(required(columns[2], "serviceType"));
        parsedBatchFile.setGeneratedAt(parseDateTime(columns[3], "generatedAt"));
        parsedBatchFile.setSourceAccountNumber(required(columns[4], "sourceAccountNumber"));
        parsedBatchFile.setHeaderTotalRecords(parseInteger(columns[5], "headerTotalRecords"));
        parsedBatchFile.setHeaderControlAmount(parseAmount(columns[6], "headerControlAmount"));
    }

    private ParsedPaymentLine parseDetail(String[] columns) {
        if (columns.length != 9) {
            throw new BadRequestException("Cada detalle debe tener 9 columnas.");
        }
        ParsedPaymentLine parsedPaymentLine = new ParsedPaymentLine();
        parsedPaymentLine.setSequenceNumber(parseInteger(columns[1], "sequenceNumber"));
        parsedPaymentLine.setBeneficiaryIdentification(required(columns[2], "beneficiaryIdentification"));
        parsedPaymentLine.setBeneficiaryName(required(columns[3], "beneficiaryName"));
        parsedPaymentLine.setDestinationAccountNumber(required(columns[4], "destinationAccountNumber"));
        parsedPaymentLine.setRoutingCode(required(columns[5], "routingCode"));
        parsedPaymentLine.setAmount(parseAmount(columns[6], "amount"));
        parsedPaymentLine.setReference(required(columns[7], "reference"));
        parsedPaymentLine.setNotificationEmail(required(columns[8], "notificationEmail"));
        return parsedPaymentLine;
    }

    private void parseFooter(String[] columns, ParsedBatchFile parsedBatchFile) {
        if (columns.length != 4) {
            throw new BadRequestException("El pie de control debe tener 4 columnas.");
        }
        parsedBatchFile.setSecurityHash(required(columns[1], "securityHash"));
        parsedBatchFile.setFooterTotalRecords(parseInteger(columns[2], "footerTotalRecords"));
        parsedBatchFile.setFooterControlAmount(parseAmount(columns[3], "footerControlAmount"));
    }

    private String[] split(String rawLine) {
        return rawLine.split(",", -1);
    }

    private String required(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException("Campo obligatorio ausente: " + fieldName);
        }
        return value.trim();
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.valueOf(required(value, fieldName));
        } catch (NumberFormatException exception) {
            throw new BadRequestException("Campo numerico invalido: " + fieldName);
        }
    }

    private BigDecimal parseAmount(String value, String fieldName) {
        try {
            return new BigDecimal(required(value, fieldName));
        } catch (NumberFormatException exception) {
            throw new BadRequestException("Monto invalido: " + fieldName);
        }
    }

    private OffsetDateTime parseDateTime(String value, String fieldName) {
        try {
            return OffsetDateTime.parse(required(value, fieldName));
        } catch (DateTimeParseException exception) {
            throw new BadRequestException("Fecha invalida: " + fieldName);
        }
    }
}
