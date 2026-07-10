package com.healthcare.billingservice.config;

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
    public static final String APPOINTMENT_EVENTS_QUEUE = "billing.appointment.events";

    @Bean
    TopicExchange healthcareEventsExchange() {
        return new TopicExchange(HEALTHCARE_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    Queue appointmentEventsQueue() {
        return new Queue(APPOINTMENT_EVENTS_QUEUE, true);
    }

    @Bean
    Binding appointmentEventsBinding(Queue appointmentEventsQueue, TopicExchange healthcareEventsExchange) {
        return BindingBuilder.bind(appointmentEventsQueue).to(healthcareEventsExchange).with("appointment.booked");
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}