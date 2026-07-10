package com.healthcare.appointmentservice.api;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AppointmentRequest(
        @NotBlank String patientId,
        @NotBlank @Size(max = 64) String providerReference,
        @NotNull @Future LocalDateTime scheduledAt,
        @NotBlank @Size(max = 32) String status,
        @Size(max = 255) String reason
) {
}