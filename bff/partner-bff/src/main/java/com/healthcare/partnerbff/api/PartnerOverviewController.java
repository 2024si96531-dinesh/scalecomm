package com.healthcare.partnerbff.api;

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
@Tag(name = "Partner Overview")
public class PartnerOverviewController {

    private final RestClient restClient = RestClient.create();

    @Value("${app.clients.insurance-service-base-url}")
    private String insuranceServiceBaseUrl;

    @Value("${app.clients.appointment-service-base-url}")
    private String appointmentServiceBaseUrl;

    @Value("${app.clients.billing-service-base-url}")
    private String billingServiceBaseUrl;

    @GetMapping("/partner-overview")
    public Map<String, Object> partnerOverview(@RequestParam String patientId) {
        return Map.of(
                "policies", fetch(insuranceServiceBaseUrl + "/api/v1/policies?patientId=" + patientId),
                "appointments", fetch(appointmentServiceBaseUrl + "/api/v1/appointments?patientId=" + patientId),
                "invoices", fetch(billingServiceBaseUrl + "/api/v1/invoices?patientId=" + patientId)
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