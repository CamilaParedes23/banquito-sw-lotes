package com.banquito.switchpagos.batch.controller;

import com.banquito.switchpagos.batch.exception.BadRequestException;
import com.banquito.switchpagos.batch.service.BatchService;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class BatchControllerTest {

    @Test
    void shouldRequireCompanyRucForPublicHttpUpload() {
        BatchService batchService = mock(BatchService.class);
        BatchController controller = new BatchController(batchService);
        MultipartFile file = mock(MultipartFile.class);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> controller.uploadBatch(file, null, "PORTAL_WEB", "frontend-user"));

        assertEquals("COMPANY_RUC_REQUIRED", exception.getCode());
        assertEquals("El RUC de la empresa autenticada es obligatorio para cargar el lote.",
                exception.getMessage());
        verify(batchService, never()).uploadBatch(file, null, "PORTAL_WEB", "frontend-user");
    }
}
