package com.healthcare.notificationservice.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ServiceInfoController {

    @GetMapping("/service-info")
    public Map<String, Object> serviceInfo() {
        return Map.of(
                "service", "NotificationService",
                "responsibilities", List.of(
                        "Queue notifications for outbound delivery",
                        "Track notification delivery state and retry attempts"
                ),
                "keyApiOperations", List.of(
                        "POST /api/v1/notifications",
                        "GET /api/v1/notifications/{notificationId}",
                        "GET /api/v1/notifications/{notificationId}/status"
                ),
                "collaborations", List.of(
                        Map.of("with", "AppointmentService", "type", "event", "purpose", "Consume appointment lifecycle updates"),
                        Map.of("with", "BillingService", "type", "event", "purpose", "Consume billing status updates"),
                        Map.of("with", "AuthService", "type", "event", "purpose", "Consume user lifecycle updates"),
                        Map.of("with", "PharmacyService", "type", "event", "purpose", "Consume prescription fulfillment updates"),
                        Map.of("with", "InsuranceService", "type", "event", "purpose", "Consume claim result updates")
                )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "NotificationService");
    }
}