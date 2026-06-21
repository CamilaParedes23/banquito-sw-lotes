package com.banquito.switchpagos.batch.client;

import com.banquito.switchpagos.batch.dto.response.CoreAccountResponse;
import com.banquito.switchpagos.batch.exception.CoreAccountClientException;
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

class CoreAccountClientTest {

    private static final String ACCOUNT_PATH = "/api/v1/accounts/{accountNumber}";

    @Test
    void shouldResolveAccountByNumberUsingBearerToken() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://kong-gateway:8000");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CoreKongTokenProvider tokenProvider = mock(CoreKongTokenProvider.class);
        when(tokenProvider.getBearerToken("account.read")).thenReturn("technical-token");
        CoreAccountClient client = new CoreAccountClient(builder.build(), tokenProvider, ACCOUNT_PATH, "account.read");

        server.expect(requestTo("http://kong-gateway:8000/api/v1/accounts/0010000010599"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer technical-token"))
                .andRespond(withSuccess("""
                        {
                          "accountUuid": "58c78625-39fa-4b3e-897a-6be2fb680852",
                          "accountNumber": "0010000010599",
                          "customerUuid": "3f26a20e-c149-5666-84b9-7c8ce0ed2712",
                          "identification": "1792103456001",
                          "status": "ACTIVA",
                          "massPaymentMainAccount": true,
                          "accountPurpose": "MASS_PAYMENTS"
                        }
                        """, MediaType.APPLICATION_JSON));

        CoreAccountResponse response = client.findByAccountNumber("0010000010599");

        assertEquals("0010000010599", response.getAccountNumber());
        assertEquals("3f26a20e-c149-5666-84b9-7c8ce0ed2712", response.getCustomerUuid());
        assertEquals(Boolean.TRUE, response.getMassPaymentMainAccount());
        server.verify();
    }

    @Test
    void shouldTranslateNotFoundToSourceAccountNotFound() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://kong-gateway:8000");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CoreKongTokenProvider tokenProvider = mock(CoreKongTokenProvider.class);
        when(tokenProvider.getBearerToken("account.read")).thenReturn("technical-token");
        CoreAccountClient client = new CoreAccountClient(builder.build(), tokenProvider, ACCOUNT_PATH, "account.read");

        server.expect(requestTo("http://kong-gateway:8000/api/v1/accounts/0000000000000"))
                .andRespond(withResourceNotFound());

        CoreAccountClientException exception = assertThrows(
                CoreAccountClientException.class,
                () -> client.findByAccountNumber("0000000000000"));

        assertEquals("SOURCE_ACCOUNT_NOT_FOUND", exception.getCode());
        server.verify();
    }
}
