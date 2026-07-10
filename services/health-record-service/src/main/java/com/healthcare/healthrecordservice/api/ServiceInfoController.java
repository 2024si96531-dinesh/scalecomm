package com.healthcare.healthrecordservice.api;

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
                "service", "HealthRecordService",
                "responsibilities", List.of(
                        "Manage longitudinal health records",
                        "Provide patient clinical history lookups"
                ),
                "keyApiOperations", List.of(
                        "POST /api/v1/records",
                        "GET /api/v1/records/{recordId}",
                        "GET /api/v1/patients/{patientId}/records"
                ),
                "collaborations", List.of(
                        Map.of("with", "PatientService", "type", "query", "purpose", "Validate patient ownership before record creation"),
                        Map.of("with", "PharmacyService", "type", "event", "purpose", "Emit prescription-relevant clinical updates")
                )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "HealthRecordService");
    }
}