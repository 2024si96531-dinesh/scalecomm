package com.healthcare.billingservice.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record InvoiceResponse(
        String invoiceId,
        String patientId,
        String appointmentId,
        BigDecimal amount,
        String currency,
        String status,
        LocalDateTime issuedAt,
        LocalDateTime dueAt,
        Instant createdAt,
        Instant updatedAt
) {
}