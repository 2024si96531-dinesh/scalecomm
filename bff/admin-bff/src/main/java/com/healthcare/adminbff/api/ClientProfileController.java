package com.healthcare.adminbff.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ClientProfileController {

    @GetMapping("/client-profile")
    public Map<String, Object> clientProfile() {
        return Map.of(
                "client", "admin-portal",
                "responsibilities", List.of(
                        "Aggregate operational, billing, and access-control views for administrators",
                        "Expose administration-focused APIs without coupling clients to internal services"
                ),
                "upstreamServices", List.of(
                        "BillingService",
                        "InsuranceService",
                        "AuthService",
                        "NotificationService"
                ),
                "interactionStyles", List.of("REST query", "REST command", "async event consumption")
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "AdminBff");
    }
}