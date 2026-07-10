package com.healthcare.appointmentservice.api;

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
                "service", "AppointmentService",
                "responsibilities", List.of(
                        "Manage appointment booking lifecycle",
                        "Coordinate schedule changes with downstream services"
                ),
                "keyApiOperations", List.of(
                        "POST /api/v1/appointments",
                        "GET /api/v1/appointments/{appointmentId}",
                        "PUT /api/v1/appointments/{appointmentId}/reschedule",
                        "PUT /api/v1/appointments/{appointmentId}/cancel"
                ),
                "collaborations", List.of(
                        Map.of("with", "PatientService", "type", "query", "purpose", "Validate patient identity before booking"),
                        Map.of("with", "BillingService", "type", "event", "purpose", "Emit billable appointment lifecycle changes"),
                        Map.of("with", "NotificationService", "type", "event", "purpose", "Emit patient-facing schedule notifications")
                )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "AppointmentService");
    }
}