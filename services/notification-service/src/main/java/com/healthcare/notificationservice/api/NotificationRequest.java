package com.healthcare.notificationservice.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotificationRequest(
        @NotBlank @Size(max = 32) String channel,
        @NotBlank @Size(max = 150) String recipient,
        @Size(max = 200) String subject,
        @NotBlank String body,
        @NotBlank @Size(max = 32) String status
) {
}