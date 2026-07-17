package com.healthcare.mobilebff.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Mobile Overview")
public class MobileOverviewController {

    private final RestClient restClient = RestClient.create();

    @Value("${app.clients.patient-service-base-url}")
    private String patientServiceBaseUrl;

    @Value("${app.clients.appointment-service-base-url}")
    private String appointmentServiceBaseUrl;

    @Value("${app.clients.pharmacy-service-base-url}")
    private String pharmacyServiceBaseUrl;

    @GetMapping("/mobile-overview")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    public Map<String, Object> mobileOverview(@RequestParam String patientId) {
        return Map.of(
                "patient", fetch(patientServiceBaseUrl + "/api/v1/patients/" + patientId),
                "appointments", fetch(appointmentServiceBaseUrl + "/api/v1/appointments?patientId=" + patientId),
                "prescriptions", fetch(pharmacyServiceBaseUrl + "/api/v1/prescriptions?patientId=" + patientId)
        );
    }

    private Object fetch(String url) {
        try {
            String authorization = currentHeader("Authorization");
            String correlationId = currentHeader("X-Correlation-Id");

            ResponseEntity<Object> response = restClient.get()
                    .uri(url)
                    .headers(httpHeaders -> {
                        if (authorization != null && !authorization.isBlank()) {
                            httpHeaders.set("Authorization", authorization);
                        }
                        if (correlationId != null && !correlationId.isBlank()) {
                            httpHeaders.set("X-Correlation-Id", correlationId);
                        }
                    })
                    .retrieve()
                    .toEntity(Object.class);
            return response.getBody();
        } catch (Exception exception) {
            return Map.of("url", url, "error", exception.getClass().getSimpleName());
        }
    }

    private String currentHeader(String name) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(name);
    }
}