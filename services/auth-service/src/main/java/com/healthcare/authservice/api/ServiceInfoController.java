package com.healthcare.authservice.api;

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
                "service", "AuthService",
                "responsibilities", List.of(
                        "Manage system user accounts",
                        "Maintain authorization role assignments"
                ),
                "keyApiOperations", List.of(
                        "POST /api/v1/auth/login",
                        "POST /api/v1/users",
                        "GET /api/v1/users/{userId}",
                        "GET /api/v1/users"
                ),
                "collaborations", List.of(
                        Map.of("with", "WebBff", "type", "query", "purpose", "Support authenticated client access"),
                        Map.of("with", "NotificationService", "type", "event", "purpose", "Emit user lifecycle events for messaging")
                )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "AuthService");
    }
}