package com.healthcare.billingservice.messaging;

import java.time.Instant;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.healthcare.billingservice.config.MessagingConfig;
import com.healthcare.billingservice.domain.Invoice;

@Component
public class InvoiceEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final boolean messagingEnabled;

    public InvoiceEventPublisher(RabbitTemplate rabbitTemplate, @Value("${app.messaging.enabled:true}") boolean messagingEnabled) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingEnabled = messagingEnabled;
    }

    public void publishIssued(Invoice invoice) {
        if (!messagingEnabled) {
            return;
        }
        rabbitTemplate.convertAndSend(MessagingConfig.HEALTHCARE_EVENTS_EXCHANGE, "invoice.issued",
                new InvoiceIssuedEvent("InvoiceIssued", invoice.getInvoiceId(), invoice.getPatientId(), invoice.getAppointmentId(), invoice.getAmount(), invoice.getStatus(), Instant.now()));
    }
}