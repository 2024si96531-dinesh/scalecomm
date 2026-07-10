package com.healthcare.webbff.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Web Dashboard")
public class WebDashboardController {

    private final RestClient restClient = RestClient.create();

    @Value("${app.clients.patient-service-base-url}")
    private String patientServiceBaseUrl;

    @Value("${app.clients.appointment-service-base-url}")
    private String appointmentServiceBaseUrl;

    @Value("${app.clients.health-record-service-base-url}")
    private String healthRecordServiceBaseUrl;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@RequestParam String patientId) {
        return Map.of(
                "patient", fetch(patientServiceBaseUrl + "/api/v1/patients/" + patientId),
                "appointments", fetch(appointmentServiceBaseUrl + "/api/v1/appointments?patientId=" + patientId),
                "records", fetch(healthRecordServiceBaseUrl + "/api/v1/records?patientId=" + patientId)
        );
    }

    private Object fetch(String url) {
        try {
            ResponseEntity<Object> response = restClient.get().uri(url).retrieve().toEntity(Object.class);
            return response.getBody();
        } catch (Exception exception) {
            return Map.of("url", url, "error", exception.getClass().getSimpleName());
        }
    }
}