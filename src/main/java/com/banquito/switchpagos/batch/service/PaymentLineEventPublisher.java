package com.banquito.switchpagos.batch.service;

import com.banquito.switchpagos.batch.dto.event.PaymentLineRequestedEvent;

public interface PaymentLineEventPublisher {

    void publish(PaymentLineRequestedEvent event);
}
