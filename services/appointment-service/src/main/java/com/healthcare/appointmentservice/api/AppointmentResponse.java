package com.healthcare.appointmentservice.api;

import java.time.Instant;
import java.time.LocalDateTime;

public record AppointmentResponse(
        String appointmentId,
        String patientId,
        String providerReference,
        LocalDateTime scheduledAt,
        String status,
        String reason,
        Instant createdAt,
        Instant updatedAt
) {
}