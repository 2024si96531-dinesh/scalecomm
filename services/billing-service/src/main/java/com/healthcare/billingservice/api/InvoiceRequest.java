package com.healthcare.billingservice.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InvoiceRequest(
        @NotBlank String patientId,
        String appointmentId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String status,
        @NotNull LocalDateTime issuedAt,
        LocalDateTime dueAt
) {
}