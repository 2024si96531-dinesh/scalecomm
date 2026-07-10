package com.healthcare.patientservice.api;

import java.time.Instant;
import java.time.LocalDate;

public record PatientResponse(
        String patientId,
        String externalReference,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        String phone,
        String email,
        String status,
        Instant createdAt,
        Instant updatedAt
) {
}