package com.banquito.switchpagos.batch.client;

import com.banquito.switchpagos.batch.dto.request.CoreFundingReleaseRequest;
import com.banquito.switchpagos.batch.dto.request.CoreFundingRequest;
import com.banquito.switchpagos.batch.dto.request.CorePaymentReservationRequest;
import com.banquito.switchpagos.batch.dto.response.CoreFundingReleaseResponse;
import com.banquito.switchpagos.batch.dto.response.CoreFundingResponse;
import com.banquito.switchpagos.batch.dto.response.CorePaymentReservationResponse;
import com.banquito.switchpagos.batch.exception.CoreBankingClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CoreBankingClient {

    private static final BigDecimal ZERO_COMMISSION = BigDecimal.ZERO;

    private final RestClient coreKongRestClient;
    private final String switchCorePath;
    private final String paymentReservationsPath;
    private final CoreKongTokenProvider tokenProvider;
    private final String reservationCreateRequiredScope;

    public CoreBankingClient(
            @Qualifier("coreKongRestClient") RestClient coreKongRestClient,
            @Value("${core.kong.switch-core-path}") String switchCorePath,
            @Value("${core.kong.payment-reservations-path}") String paymentReservationsPath,
            CoreKongTokenProvider tokenProvider,
            @Value("${core.kong.reservation-create-required-scope}") String reservationCreateRequiredScope) {
        this.coreKongRestClient = coreKongRestClient;
        this.switchCorePath = switchCorePath;
        this.paymentReservationsPath = paymentReservationsPath;
        this.tokenProvider = tokenProvider;
        this.reservationCreateRequiredScope = reservationCreateRequiredScope;
    }

    public CoreFundingResponse requestFunding(CoreFundingRequest request) {
        validateFundingRequest(request);
        CorePaymentReservationRequest reservationRequest = toReservationRequest(request);
        try {
            CorePaymentReservationResponse reservationResponse = coreKongRestClient.post()
                    .uri(buildPaymentReservationsUri())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(this::addAuthorizationHeader)
                    .body(reservationRequest)
                    .retrieve()
                    .body(CorePaymentReservationResponse.class);
            return toFundingResponse(request, reservationResponse);
        } catch (RestClientResponseException exception) {
            throw toCoreBankingException(exception);
        } catch (ResourceAccessException exception) {
            throw new CoreBankingClientException(
                    "Core REST via Kong no disponible o timeout al crear la reserva de pago.",
                    exception,
                    null,
                    false);
        } catch (IllegalStateException exception) {
            throw new CoreBankingClientException(
                    "No fue posible obtener token tecnico para llamar Core REST via Kong.",
                    exception,
                    null,
                    false);
        }
    }

    public CoreFundingReleaseResponse releaseFunding(String coreFundingId, CoreFundingReleaseRequest request) {
        throw new CoreBankingClientException(
                "ReleaseFunding es legacy/deprecado y no se usa en batch-service bajo la arquitectura REST/Kong.");
    }

    private void validateFundingRequest(CoreFundingRequest request) {
        if (request == null) {
            throw new CoreBankingClientException("La solicitud de reserva al Core no puede ser nula.");
        }
        if (request.getBatchId() == null) {
            throw new CoreBankingClientException("La solicitud de reserva requiere batchId.");
        }
        if (request.getCorrelationId() == null) {
            throw new CoreBankingClientException("La solicitud de reserva requiere correlationId.");
        }
        if (!StringUtils.hasText(request.getCompanyCustomerUuid())) {
            throw new CoreBankingClientException(
                    "La solicitud de reserva requiere el customerUuid de la empresa resuelto en Core.");
        }
        if (!StringUtils.hasText(request.getMainAccountNumber())) {
            throw new CoreBankingClientException("La solicitud de reserva requiere mainAccountNumber.");
        }
        if (request.getTotalAmount() == null || request.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreBankingClientException("La solicitud de reserva requiere totalAmount mayor a cero.");
        }
        if (request.getAccountingDate() == null) {
            throw new CoreBankingClientException("La solicitud de reserva requiere accountingDate.");
        }
    }

    private CorePaymentReservationRequest toReservationRequest(CoreFundingRequest request) {
        CorePaymentReservationRequest reservationRequest = new CorePaymentReservationRequest();
        reservationRequest.setBatchId(request.getBatchId());
        reservationRequest.setCorrelationId(request.getCorrelationId());
        reservationRequest.setCompanyCustomerUuid(request.getCompanyCustomerUuid());
        reservationRequest.setMainAccountNumber(request.getMainAccountNumber());
        reservationRequest.setTotalAmount(request.getTotalAmount());
        reservationRequest.setCommissionAmount(resolveCommissionAmount(request));
        reservationRequest.setChannel(request.getChannel());
        reservationRequest.setAccountingDate(request.getAccountingDate());
        return reservationRequest;
    }

    private BigDecimal resolveCommissionAmount(CoreFundingRequest request) {
        return request.getCommissionAmount() == null ? ZERO_COMMISSION : request.getCommissionAmount();
    }

    private CoreFundingResponse toFundingResponse(
            CoreFundingRequest request,
            CorePaymentReservationResponse reservationResponse) {
        if (reservationResponse == null || reservationResponse.getReservationUuid() == null) {
            throw new CoreBankingClientException(
                    "Core REST via Kong no devolvio reservationUuid para la reserva de pago.",
                    null,
                    null,
                    false);
        }
        CoreFundingResponse response = new CoreFundingResponse();
        response.setCoreFundingId(reservationResponse.getReservationUuid().toString());
        response.setBatchId(reservationResponse.getBatchId() == null ? request.getBatchId() : reservationResponse.getBatchId());
        response.setStatus(reservationResponse.getStatus());
        response.setFundedAmount(reservationResponse.getReservedAmount());
        response.setAccountingDate(request.getAccountingDate());
        response.setCoreTransactionId(null);
        response.setMessage("Reserva Core REST/Kong creada con estado " + reservationResponse.getStatus());
        return response;
    }

    private CoreBankingClientException toCoreBankingException(RestClientResponseException exception) {
        Integer status = exception.getStatusCode().value();
        Boolean functionalRejection = status == 400 || status == 404 || status == 409 || status == 422;
        return new CoreBankingClientException(
                "Core REST via Kong rechazo la reserva. HTTP " + status + ": " + sanitizeBody(exception.getResponseBodyAsString()),
                exception,
                status,
                functionalRejection);
    }

    private String sanitizeBody(String responseBody) {
        if (!StringUtils.hasText(responseBody)) {
            return "sin detalle";
        }
        String compactBody = responseBody.replaceAll("\\s+", " ").trim();
        if (compactBody.length() > 300) {
            return compactBody.substring(0, 300);
        }
        return compactBody;
    }

    private void addAuthorizationHeader(HttpHeaders headers) {
        headers.setBearerAuth(tokenProvider.getBearerToken(reservationCreateRequiredScope));
    }

    private String buildPaymentReservationsUri() {
        return normalizePath(switchCorePath) + normalizePath(paymentReservationsPath);
    }

    private String normalizePath(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }
        String normalizedPath = path.trim();
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }
        if (normalizedPath.endsWith("/")) {
            return normalizedPath.substring(0, normalizedPath.length() - 1);
        }
        return normalizedPath;
    }
}
