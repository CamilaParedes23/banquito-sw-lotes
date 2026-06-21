package com.banquito.switchpagos.batch.client;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoreKongTokenProviderTest {

    private final CoreKongTokenProvider provider = new CoreKongTokenProvider(
            "http://localhost:8000",
            "",
            true,
            "switch-pagos-internos-service",
            "secret",
            "core.reserve.consume",
            "/api/v1/auth/client-token",
            60L,
            1000L,
            1000L);

    @Test
    void shouldReadExpiresInSeconds() {
        Long ttl = provider.extractExpiresIn(Map.of("expiresInSeconds", 900));

        assertEquals(840L, ttl);
    }

    @Test
    void shouldReadExpiresInForBackwardCompatibility() {
        Long ttl = provider.extractExpiresIn(Map.of("expiresIn", 900));

        assertEquals(840L, ttl);
    }

    @Test
    void shouldPreferExpiresAtWhenPresent() {
        String expiresAt = OffsetDateTime.now().plusSeconds(300).toString();

        Long ttl = provider.extractExpiresIn(Map.of(
                "expiresAt", expiresAt,
                "expiresInSeconds", 900));

        assertTrue(ttl > 1L);
        assertTrue(ttl <= 300L);
    }

    @Test
    void shouldUseFallbackWhenNoExplicitExpirationExists() {
        Long ttl = provider.extractExpiresIn(Map.of());

        assertEquals(840L, ttl);
    }
}
