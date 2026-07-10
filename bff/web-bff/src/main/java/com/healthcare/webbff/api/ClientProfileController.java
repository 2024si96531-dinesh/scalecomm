package com.healthcare.webbff.api;

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
                "client", "web-portal",
                "responsibilities", List.of(
                        "Aggregate patient, appointment, and health record views for browser clients",
                        "Expose a client-specific edge API without leaking internal service topology"
                ),
                "upstreamServices", List.of(
                        "PatientService",
                        "AppointmentService",
                        "HealthRecordService",
                        "AuthService"
                ),
                "interactionStyles", List.of("REST query", "REST command", "async event consumption")
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "WebBff");
    }
}