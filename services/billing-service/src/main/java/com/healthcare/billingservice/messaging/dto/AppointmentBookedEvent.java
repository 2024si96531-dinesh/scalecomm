package com.healthcare.billingservice.messaging.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public record AppointmentBookedEvent(
        String eventType,
        String appointmentId,
        String patientId,
        LocalDateTime scheduledAt,
        String status,
        Instant occurredAt
) {
}