package com.banquito.switchpagos.batch.client;

import com.banquito.switchpagos.batch.dto.response.CoreCustomerResponse;
import com.banquito.switchpagos.batch.exception.CoreCustomerClientException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class CoreCustomerClientTest {

    private static final String CUSTOMER_PATH = "/api/v1/customers/by-identification/{identification}";

    @Test
    void shouldResolveCustomerByCompanyRucUsingBearerToken() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://kong-gateway:8000");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CoreKongTokenProvider tokenProvider = mock(CoreKongTokenProvider.class);
        when(tokenProvider.getBearerToken("customer.read")).thenReturn("technical-token");
        CoreCustomerClient client = new CoreCustomerClient(builder.build(), tokenProvider, CUSTOMER_PATH, "customer.read");
        UUID customerUuid = UUID.randomUUID();

        server.expect(requestTo("http://kong-gateway:8000/api/v1/customers/by-identification/1792103456001"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer technical-token"))
                .andRespond(withSuccess("""
                        {
                          "customerUuid": "%s",
                          "customerType": "JURIDICO",
                          "identificationType": "RUC",
                          "identification": "1792103456001",
                          "displayName": "Empresa Demo",
                          "status": "ACTIVO",
                          "massPaymentsEnabled": true
                        }
                        """.formatted(customerUuid), MediaType.APPLICATION_JSON));

        CoreCustomerResponse response = client.findByIdentification("1792103456001");

        assertEquals(customerUuid, response.getCustomerUuid());
        assertEquals("1792103456001", response.getIdentification());
        assertEquals(Boolean.TRUE, response.getMassPaymentsEnabled());
        server.verify();
    }

    @Test
    void shouldTranslateNotFoundToCompanyNotFound() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://kong-gateway:8000");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CoreKongTokenProvider tokenProvider = mock(CoreKongTokenProvider.class);
        when(tokenProvider.getBearerToken("customer.read")).thenReturn("technical-token");
        CoreCustomerClient client = new CoreCustomerClient(builder.build(), tokenProvider, CUSTOMER_PATH, "customer.read");

        server.expect(requestTo("http://kong-gateway:8000/api/v1/customers/by-identification/9999999999999"))
                .andRespond(withResourceNotFound());

        CoreCustomerClientException exception = assertThrows(
                CoreCustomerClientException.class,
                () -> client.findByIdentification("9999999999999"));

        assertEquals("COMPANY_NOT_FOUND", exception.getCode());
        server.verify();
    }
}
