package com.healthcare.billingservice.api;

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
                "service", "BillingService",
                "responsibilities", List.of(
                        "Manage invoice lifecycle",
                        "Track payment capture and settlement status"
                ),
                "keyApiOperations", List.of(
                        "POST /api/v1/invoices",
                        "GET /api/v1/invoices/{invoiceId}",
                        "POST /api/v1/payments",
                        "GET /api/v1/payments/{paymentId}"
                ),
                "collaborations", List.of(
                        Map.of("with", "AppointmentService", "type", "event", "purpose", "Consume billable appointment lifecycle changes"),
                        Map.of("with", "InsuranceService", "type", "event", "purpose", "Publish claim-eligible billing changes"),
                        Map.of("with", "NotificationService", "type", "event", "purpose", "Emit billing updates for user notification")
                )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "BillingService");
    }
}