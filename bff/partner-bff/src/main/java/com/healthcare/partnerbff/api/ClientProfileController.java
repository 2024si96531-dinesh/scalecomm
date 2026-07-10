package com.healthcare.partnerbff.api;

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
                "client", "partner-portal",
                "responsibilities", List.of(
                        "Aggregate partner-facing insurance, billing, and scheduling views",
                        "Isolate partner clients from direct service-to-service dependencies"
                ),
                "upstreamServices", List.of(
                        "InsuranceService",
                        "AppointmentService",
                        "BillingService",
                        "AuthService"
                ),
                "interactionStyles", List.of("REST query", "REST command", "async event consumption")
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "PartnerBff");
    }
}