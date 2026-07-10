package com.healthcare.patientservice.messaging;

import java.time.Instant;

public record PatientEvent(
        String eventType,
        String patientId,
        String firstName,
        String lastName,
        String status,
        Instant occurredAt
) {
}