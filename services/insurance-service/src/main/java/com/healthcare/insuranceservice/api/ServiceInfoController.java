package com.healthcare.insuranceservice.api;

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
                "service", "InsuranceService",
                "responsibilities", List.of(
                        "Manage patient coverage policies",
                        "Track claim submission and adjudication status"
                ),
                "keyApiOperations", List.of(
                        "POST /api/v1/policies",
                        "GET /api/v1/policies/{policyId}",
                        "POST /api/v1/claims",
                        "GET /api/v1/claims/{claimId}"
                ),
                "collaborations", List.of(
                        Map.of("with", "BillingService", "type", "event", "purpose", "Consume invoice lifecycle changes for claims"),
                        Map.of("with", "PatientService", "type", "query", "purpose", "Validate patient policy ownership"),
                        Map.of("with", "NotificationService", "type", "event", "purpose", "Emit claim result updates")
                )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "InsuranceService");
    }
}