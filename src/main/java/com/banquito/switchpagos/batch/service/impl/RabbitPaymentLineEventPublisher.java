package com.banquito.switchpagos.batch.service.impl;

import com.banquito.switchpagos.batch.dto.event.PaymentLineRequestedEvent;
import com.banquito.switchpagos.batch.service.PaymentLineEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitPaymentLineEventPublisher implements PaymentLineEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public RabbitPaymentLineEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbit.exchange.batch}") String exchangeName,
            @Value("${rabbit.routing-key.payment-line-requested}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    @Override
    public void publish(PaymentLineRequestedEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
    }
}
