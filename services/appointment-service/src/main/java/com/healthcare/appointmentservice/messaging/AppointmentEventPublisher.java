package com.healthcare.appointmentservice.messaging;

import java.time.Instant;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.healthcare.appointmentservice.config.MessagingConfig;
import com.healthcare.appointmentservice.domain.Appointment;

@Component
public class AppointmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final boolean messagingEnabled;

    public AppointmentEventPublisher(RabbitTemplate rabbitTemplate, @Value("${app.messaging.enabled:true}") boolean messagingEnabled) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingEnabled = messagingEnabled;
    }

    public void publishBooked(Appointment appointment) {
        publish("appointment.booked", appointment, "AppointmentBooked");
    }

    public void publishUpdated(Appointment appointment) {
        publish("appointment.updated", appointment, "AppointmentUpdated");
    }

    private void publish(String routingKey, Appointment appointment, String eventType) {
        if (!messagingEnabled) {
            return;
        }
        rabbitTemplate.convertAndSend(MessagingConfig.HEALTHCARE_EVENTS_EXCHANGE, routingKey,
                new AppointmentEvent(eventType, appointment.getAppointmentId(), appointment.getPatientId(), appointment.getScheduledAt(), appointment.getStatus(), Instant.now()));
    }
}