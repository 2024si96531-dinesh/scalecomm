package com.healthcare.notificationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String HEALTHCARE_EVENTS_EXCHANGE = "healthcare.events";
    public static final String NOTIFICATION_EVENTS_QUEUE = "notification.domain.events";

    @Bean
    TopicExchange healthcareEventsExchange() {
        return new TopicExchange(HEALTHCARE_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    Queue notificationEventsQueue() {
        return new Queue(NOTIFICATION_EVENTS_QUEUE, true);
    }

    @Bean
    Binding patientCreatedBinding(Queue notificationEventsQueue, TopicExchange healthcareEventsExchange) {
        return BindingBuilder.bind(notificationEventsQueue).to(healthcareEventsExchange).with("patient.*");
    }

    @Bean
    Binding appointmentBinding(Queue notificationEventsQueue, TopicExchange healthcareEventsExchange) {
        return BindingBuilder.bind(notificationEventsQueue).to(healthcareEventsExchange).with("appointment.*");
    }

    @Bean
    Binding invoiceBinding(Queue notificationEventsQueue, TopicExchange healthcareEventsExchange) {
        return BindingBuilder.bind(notificationEventsQueue).to(healthcareEventsExchange).with("invoice.*");
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}