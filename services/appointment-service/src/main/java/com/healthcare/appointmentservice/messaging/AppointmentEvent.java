package com.healthcare.appointmentservice.messaging;

import java.time.Instant;
import java.time.LocalDateTime;

public record AppointmentEvent(
        String eventType,
        String appointmentId,
        String patientId,
        LocalDateTime scheduledAt,
        String status,
        Instant occurredAt
) {
}