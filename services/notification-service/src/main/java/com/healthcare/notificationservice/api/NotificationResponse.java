package com.healthcare.notificationservice.api;

import java.time.Instant;
import java.time.LocalDateTime;

public record NotificationResponse(
        String notificationId,
        String channel,
        String recipient,
        String subject,
        String body,
        String status,
        Instant createdAt,
        LocalDateTime sentAt
) {
}