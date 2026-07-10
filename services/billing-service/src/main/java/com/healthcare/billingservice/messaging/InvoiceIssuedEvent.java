package com.healthcare.billingservice.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record InvoiceIssuedEvent(
        String eventType,
        String invoiceId,
        String patientId,
        String appointmentId,
        BigDecimal amount,
        String status,
        Instant occurredAt
) {
}