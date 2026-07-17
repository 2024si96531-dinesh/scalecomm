package com.healthcare.adminbff.api;

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
@Tag(name = "Admin Overview")
public class AdminOverviewController {

    private final RestClient restClient = RestClient.create();

    @Value("${app.clients.billing-service-base-url}")
    private String billingServiceBaseUrl;

    @Value("${app.clients.insurance-service-base-url}")
    private String insuranceServiceBaseUrl;

    @Value("${app.clients.notification-service-base-url}")
    private String notificationServiceBaseUrl;

    @Value("${app.clients.auth-service-base-url}")
    private String authServiceBaseUrl;

    @GetMapping("/operations-summary")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> operationsSummary(@RequestParam String patientId, @RequestParam(required = false) String username) {
        return Map.of(
                "invoices", fetch(billingServiceBaseUrl + "/api/v1/invoices?patientId=" + patientId),
                "policies", fetch(insuranceServiceBaseUrl + "/api/v1/policies?patientId=" + patientId),
                "notifications", fetch(notificationServiceBaseUrl + "/api/v1/notifications?recipient=" + patientId + "@healthcare.local"),
                "users", fetch(authServiceBaseUrl + "/api/v1/users" + (username == null ? "" : "?username=" + username))
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