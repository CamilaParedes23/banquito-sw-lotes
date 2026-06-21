package com.banquito.switchpagos.batch.client;

import com.banquito.switchpagos.batch.dto.request.CoreCompanyAccountValidationRequest;
import com.banquito.switchpagos.batch.dto.response.CoreCompanyAccountValidationResponse;
import com.banquito.switchpagos.batch.exception.CoreAccountClientException;
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

@Component
public class CoreCompanyAccountValidationClient {

    private static final String SOURCE_ACCOUNT_VALIDATION_FAILED = "SOURCE_ACCOUNT_VALIDATION_FAILED";

    private final RestClient coreKongRestClient;
    private final CoreKongTokenProvider tokenProvider;
    private final String companyAccountValidationPath;
    private final String requiredScope;

    public CoreCompanyAccountValidationClient(
            @Qualifier("coreKongRestClient") RestClient coreKongRestClient,
            CoreKongTokenProvider tokenProvider,
            @Value("${core.kong.company-account-validation-path}") String companyAccountValidationPath,
            @Value("${core.kong.company-account-validation-required-scope}") String requiredScope) {
        this.coreKongRestClient = coreKongRestClient;
        this.tokenProvider = tokenProvider;
        this.companyAccountValidationPath = companyAccountValidationPath;
        this.requiredScope = requiredScope;
    }

    public CoreCompanyAccountValidationResponse validate(
            String companyCustomerUuid,
            String mainAccountNumber,
            BigDecimal amount) {
        if (!StringUtils.hasText(companyCustomerUuid)) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "companyCustomerUuid es obligatorio para validar empresa/cuenta en Core R9I.");
        }
        if (!StringUtils.hasText(mainAccountNumber)) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "La cuenta matriz es obligatoria para validarla en Core R9I.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "El monto total del lote es obligatorio para validar cobertura en Core R9I.");
        }

        CoreCompanyAccountValidationRequest request = new CoreCompanyAccountValidationRequest();
        request.setCompanyCustomerUuid(companyCustomerUuid.trim());
        request.setMainAccountNumber(mainAccountNumber.trim());
        request.setAmount(amount);

        try {
            return coreKongRestClient.post()
                    .uri(companyAccountValidationPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(this::addAuthorizationHeader)
                    .body(request)
                    .retrieve()
                    .body(CoreCompanyAccountValidationResponse.class);
        } catch (RestClientResponseException exception) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    resolveResponseErrorMessage(exception),
                    exception,
                    exception.getStatusCode().value());
        } catch (ResourceAccessException exception) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "Core company-account-validation no esta disponible para validar empresa/cuenta.",
                    exception,
                    null);
        } catch (IllegalStateException exception) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "No fue posible obtener autorizacion tecnica para company-account-validation.",
                    exception,
                    null);
        }
    }

    private String resolveResponseErrorMessage(RestClientResponseException exception) {
        int status = exception.getStatusCode().value();
        if (status == 401 || status == 403) {
            return "Core rechazo la autorizacion tecnica para company-account-validation.";
        }
        if (status >= 500) {
            return "Core company-account-validation no esta disponible.";
        }
        return "Core rechazo la validacion empresa/cuenta. HTTP " + status + ": "
                + sanitizeBody(exception.getResponseBodyAsString());
    }

    private String sanitizeBody(String responseBody) {
        if (!StringUtils.hasText(responseBody)) {
            return "sin detalle";
        }
        String compactBody = responseBody.replaceAll("\\s+", " ").trim();
        return compactBody.length() > 300 ? compactBody.substring(0, 300) : compactBody;
    }

    private void addAuthorizationHeader(HttpHeaders headers) {
        headers.setBearerAuth(tokenProvider.getBearerToken(requiredScope));
    }
}
