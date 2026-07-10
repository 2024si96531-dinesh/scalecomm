package com.healthcare.patientservice.messaging;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.healthcare.patientservice.config.MessagingConfig;
import com.healthcare.patientservice.domain.Patient;

@Component
public class PatientEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final boolean messagingEnabled;

    public PatientEventPublisher(RabbitTemplate rabbitTemplate, @Value("${app.messaging.enabled:true}") boolean messagingEnabled) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingEnabled = messagingEnabled;
    }

    public void publishCreated(Patient patient) {
        publish("patient.created", patient, "PatientCreated");
    }

    public void publishUpdated(Patient patient) {
        publish("patient.updated", patient, "PatientUpdated");
    }

    private void publish(String routingKey, Patient patient, String eventType) {
        if (!messagingEnabled) {
            return;
        }
        rabbitTemplate.convertAndSend(
                MessagingConfig.HEALTHCARE_EVENTS_EXCHANGE,
                routingKey,
                new PatientEvent(
                        eventType,
                        patient.getPatientId(),
                        patient.getFirstName(),
                        patient.getLastName(),
                        patient.getStatus(),
                        Instant.now()
                )
        );
    }
}