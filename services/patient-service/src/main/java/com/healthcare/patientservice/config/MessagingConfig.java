package com.healthcare.patientservice.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String HEALTHCARE_EVENTS_EXCHANGE = "healthcare.events";

    @Bean
    TopicExchange healthcareEventsExchange() {
        return new TopicExchange(HEALTHCARE_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}