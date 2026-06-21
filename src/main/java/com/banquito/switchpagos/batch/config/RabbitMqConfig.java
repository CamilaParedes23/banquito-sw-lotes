package com.banquito.switchpagos.batch.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public TopicExchange batchExchange(@Value("${rabbit.exchange.batch}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange billingExchange(@Value("${rabbit.exchange.billing}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue routingPaymentLinesQueue(@Value("${rabbit.queue.routing.payment-lines}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue billingCompletedQueue(@Value("${rabbit.queue.batch.billing-completed}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding paymentLineRequestedBinding(
            TopicExchange batchExchange,
            Queue routingPaymentLinesQueue,
            @Value("${rabbit.routing-key.payment-line-requested}") String routingKey) {
        return BindingBuilder.bind(routingPaymentLinesQueue).to(batchExchange).with(routingKey);
    }

    @Bean
    public Binding billingCompletedBinding(
            TopicExchange billingExchange,
            Queue billingCompletedQueue,
            @Value("${rabbit.routing-key.billing-completed}") String routingKey) {
        return BindingBuilder.bind(billingCompletedQueue).to(billingExchange).with(routingKey);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
