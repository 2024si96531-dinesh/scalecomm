package com.healthcare.mobilebff.api;

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
                "client", "mobile-app",
                "responsibilities", List.of(
                        "Aggregate mobile-optimized patient and appointment interactions",
                        "Expose simplified task-oriented APIs for mobile clients"
                ),
                "upstreamServices", List.of(
                        "PatientService",
                        "AppointmentService",
                        "PharmacyService",
                        "NotificationService",
                        "AuthService"
                ),
                "interactionStyles", List.of("REST query", "REST command", "async event consumption")
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "MobileBff");
    }
}