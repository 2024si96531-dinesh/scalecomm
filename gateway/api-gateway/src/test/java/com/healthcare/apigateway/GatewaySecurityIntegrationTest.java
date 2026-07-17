package com.healthcare.apigateway;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.cloud.gateway.httpclient.connect-timeout=1000",
        "spring.cloud.gateway.httpclient.response-timeout=2s"
    })
@AutoConfigureWebTestClient
class GatewaySecurityIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
        void apiDocsArePublic() {
        webTestClient.get()
            .uri("/v3/api-docs")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
        void loginRouteIsPublic() {
        webTestClient.post()
            .uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"username\":\"admin1\",\"password\":\"secret123\"}")
            .exchange()
            // Gateway allows this route anonymously; 5xx is expected in isolated gateway tests.
            .expectStatus().is5xxServerError();
        }

        @Test
        void dashboardRequiresAuthenticationAndEnforcesRole() {
        webTestClient.get()
                .uri("/api/v1/dashboard?patientId=p-001")
                .exchange()
                .expectStatus().isUnauthorized();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_PARTNER")))
            .get()
            .uri("/api/v1/dashboard?patientId=p-001")
            .exchange()
            .expectStatus().isForbidden();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_PATIENT")))
            .get()
            .uri("/api/v1/dashboard?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_DOCTOR")))
            .get()
            .uri("/api/v1/dashboard?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
        void operationsSummaryRequiresAdminRole() {
        webTestClient.get()
            .uri("/api/v1/operations-summary?patientId=p-001")
            .exchange()
            .expectStatus().isUnauthorized();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_PATIENT")))
                .get()
                .uri("/api/v1/operations-summary?patientId=p-001")
                .exchange()
                .expectStatus().isForbidden();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .get()
            .uri("/api/v1/operations-summary?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();
        }

        @Test
        void partnerOverviewAllowsPartnerOrAdminOnly() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_PATIENT")))
            .get()
            .uri("/api/v1/partner-overview?patientId=p-001")
            .exchange()
            .expectStatus().isForbidden();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_PARTNER")))
            .get()
            .uri("/api/v1/partner-overview?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .get()
            .uri("/api/v1/partner-overview?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();
        }

        @Test
        void mobileOverviewAllowsPatientDoctorOrAdmin() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_PARTNER")))
            .get()
            .uri("/api/v1/mobile-overview?patientId=p-001")
            .exchange()
            .expectStatus().isForbidden();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_PATIENT")))
            .get()
            .uri("/api/v1/mobile-overview?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_DOCTOR")))
            .get()
            .uri("/api/v1/mobile-overview?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();

        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .get()
            .uri("/api/v1/mobile-overview?patientId=p-001")
            .exchange()
            .expectStatus().is5xxServerError();
    }
}
