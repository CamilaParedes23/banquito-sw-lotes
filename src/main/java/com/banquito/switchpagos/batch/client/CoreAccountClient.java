package com.banquito.switchpagos.batch.client;

import com.banquito.switchpagos.batch.dto.response.CoreAccountResponse;
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

@Component
public class CoreAccountClient {

    private static final String SOURCE_ACCOUNT_NOT_FOUND = "SOURCE_ACCOUNT_NOT_FOUND";
    private static final String SOURCE_ACCOUNT_VALIDATION_FAILED = "SOURCE_ACCOUNT_VALIDATION_FAILED";

    private final RestClient coreKongRestClient;
    private final CoreKongTokenProvider tokenProvider;
    private final String accountByNumberPath;
    private final String requiredScope;

    public CoreAccountClient(
            @Qualifier("coreKongRestClient") RestClient coreKongRestClient,
            CoreKongTokenProvider tokenProvider,
            @Value("${core.kong.account-by-number-path}") String accountByNumberPath,
            @Value("${core.kong.account-required-scope}") String requiredScope) {
        this.coreKongRestClient = coreKongRestClient;
        this.tokenProvider = tokenProvider;
        this.accountByNumberPath = accountByNumberPath;
        this.requiredScope = requiredScope;
    }

    public CoreAccountResponse findByAccountNumber(String accountNumber) {
        if (!StringUtils.hasText(accountNumber)) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "La cuenta matriz es obligatoria para validarla en Core.");
        }
        try {
            return coreKongRestClient.get()
                    .uri(accountByNumberPath, accountNumber.trim())
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(this::addAuthorizationHeader)
                    .retrieve()
                    .body(CoreAccountResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == 404) {
                throw new CoreAccountClientException(
                        SOURCE_ACCOUNT_NOT_FOUND,
                        "No se encontro la cuenta matriz informada.",
                        exception,
                        exception.getStatusCode().value());
            }
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    resolveResponseErrorMessage(exception),
                    exception,
                    exception.getStatusCode().value());
        } catch (ResourceAccessException exception) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "Core Account no esta disponible para validar la cuenta matriz.",
                    exception,
                    null);
        } catch (IllegalStateException exception) {
            throw new CoreAccountClientException(
                    SOURCE_ACCOUNT_VALIDATION_FAILED,
                    "No fue posible obtener autorizacion tecnica para validar la cuenta matriz en Core.",
                    exception,
                    null);
        }
    }

    private String resolveResponseErrorMessage(RestClientResponseException exception) {
        int status = exception.getStatusCode().value();
        if (status == 401 || status == 403) {
            return "Core rechazo la autorizacion tecnica para consultar la cuenta matriz.";
        }
        if (status >= 500) {
            return "Core Account no esta disponible para validar la cuenta matriz.";
        }
        return "No fue posible validar la cuenta matriz en Core.";
    }

    private void addAuthorizationHeader(HttpHeaders headers) {
        headers.setBearerAuth(tokenProvider.getBearerToken(requiredScope));
    }
}
