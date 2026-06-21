package com.banquito.switchpagos.batch.listener;

import com.banquito.switchpagos.batch.dto.event.BillingCompletedEvent;
import com.banquito.switchpagos.batch.enums.BatchStatus;
import com.banquito.switchpagos.batch.model.PaymentBatch;
import com.banquito.switchpagos.batch.repository.PaymentBatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class BillingCompletedListener {

    private static final Logger LOG = LoggerFactory.getLogger(BillingCompletedListener.class);

    private final PaymentBatchRepository paymentBatchRepository;

    public BillingCompletedListener(PaymentBatchRepository paymentBatchRepository) {
        this.paymentBatchRepository = paymentBatchRepository;
    }

    @RabbitListener(queues = "${rabbit.queue.batch.billing-completed}")
    @Transactional
    public void onBillingCompleted(BillingCompletedEvent event) {
        if (event == null) {
            LOG.warn("BillingCompletedEvent nulo ignorado por batch-service");
            return;
        }
        
        UUID batchId = event.getBatchId();
        LOG.info("BillingCompletedEvent recibido en batch-service. batchId={}, billingId={}", batchId, event.getBillingId());
        
        PaymentBatch batch = paymentBatchRepository.findById(batchId).orElse(null);
        if (batch == null) {
            LOG.warn("No se encontro el lote para actualizar estado a CERRADO. batchId={}", batchId);
            return;
        }
        
        // Solo actualizar si el estado actual es PROCESANDO_LINEAS
        if (!BatchStatus.PROCESANDO_LINEAS.name().equals(batch.getStatus())) {
            LOG.info("El lote no esta en estado PROCESANDO_LINEAS, no se actualiza a CERRADO. batchId={}, currentStatus={}", 
                    batchId, batch.getStatus());
            return;
        }
        
        batch.setStatus(BatchStatus.CERRADO.name());
        batch.setUpdatedAt(OffsetDateTime.now());
        paymentBatchRepository.save(batch);
        
        LOG.info("Estado del lote actualizado a CERRADO. batchId={}", batchId);
    }
}
