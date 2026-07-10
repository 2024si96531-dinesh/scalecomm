package com.healthcare.pharmacyservice.api;

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
                "service", "PharmacyService",
                "responsibilities", List.of(
                        "Manage prescriptions",
                        "Track medication dispensation status"
                ),
                "keyApiOperations", List.of(
                        "POST /api/v1/prescriptions",
                        "GET /api/v1/prescriptions/{prescriptionId}",
                        "POST /api/v1/prescriptions/{prescriptionId}/dispensations"
                ),
                "collaborations", List.of(
                        Map.of("with", "HealthRecordService", "type", "event", "purpose", "Consume clinical updates relevant to medication orders"),
                        Map.of("with", "PatientService", "type", "query", "purpose", "Validate patient context for dispensation"),
                        Map.of("with", "NotificationService", "type", "event", "purpose", "Emit fulfillment notifications")
                )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "PharmacyService");
    }
}