package com.banquito.switchpagos.batch.client;

import com.banquito.switchpagos.batch.dto.response.CoreCustomerResponse;
import com.banquito.switchpagos.batch.exception.CoreCustomerClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class CoreCustomerClient {

    private static final String COMPANY_NOT_FOUND = "COMPANY_NOT_FOUND";
    private static final String COMPANY_CUSTOMER_RESOLUTION_FAILED = "COMPANY_CUSTOMER_RESOLUTION_FAILED";

    private final RestClient coreKongRestClient;
    private final CoreKongTokenProvider tokenProvider;
    private final String customersByIdentificationPath;
    private final String requiredScope;

    public CoreCustomerClient(
            @Qualifier("coreKongRestClient") RestClient coreKongRestClient,
            CoreKongTokenProvider tokenProvider,
            @Value("${core.kong.customers-by-identification-path}") String customersByIdentificationPath,
            @Value("${core.kong.customer-required-scope}") String requiredScope) {
        this.coreKongRestClient = coreKongRestClient;
        this.tokenProvider = tokenProvider;
        this.customersByIdentificationPath = customersByIdentificationPath;
        this.requiredScope = requiredScope;
    }

    public CoreCustomerResponse findByIdentification(String companyRuc) {
        if (!StringUtils.hasText(companyRuc)) {
            throw new CoreCustomerClientException(
                    COMPANY_CUSTOMER_RESOLUTION_FAILED,
                    "El RUC de la empresa es obligatorio para resolverla en Core.");
        }
        try {
            return coreKongRestClient.get()
                    .uri(customersByIdentificationPath, companyRuc.trim())
                    .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                    .headers(this::addAuthorizationHeader)
                    .retrieve()
                    .body(CoreCustomerResponse.class);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == 404) {
                throw new CoreCustomerClientException(
                        COMPANY_NOT_FOUND,
                        "No se encontro la empresa asociada al RUC informado.",
                        exception,
                        exception.getStatusCode().value());
            }
            throw new CoreCustomerClientException(
                    COMPANY_CUSTOMER_RESOLUTION_FAILED,
                    resolveResponseErrorMessage(exception),
                    exception,
                    exception.getStatusCode().value());
        } catch (ResourceAccessException exception) {
            throw new CoreCustomerClientException(
                    COMPANY_CUSTOMER_RESOLUTION_FAILED,
                    "Core Customer no esta disponible para resolver la empresa.",
                    exception,
                    null);
        } catch (IllegalStateException exception) {
            throw new CoreCustomerClientException(
                    COMPANY_CUSTOMER_RESOLUTION_FAILED,
                    "No fue posible obtener autorizacion tecnica para consultar la empresa en Core.",
                    exception,
                    null);
        }
    }

    private String resolveResponseErrorMessage(RestClientResponseException exception) {
        int status = exception.getStatusCode().value();
        if (status == 401 || status == 403) {
            return "Core rechazo la autorizacion tecnica para consultar la empresa.";
        }
        if (status >= 500) {
            return "Core Customer no esta disponible para resolver la empresa.";
        }
        return "No fue posible resolver la empresa en Core para solicitar el fondeo.";
    }

    private void addAuthorizationHeader(HttpHeaders headers) {
        headers.setBearerAuth(tokenProvider.getBearerToken(requiredScope));
    }
}
