# Healthcare Microservices Demo Runbook

This runbook gives a live demo flow that proves:

- microservices run independently,
- APIs communicate across service boundaries,
- API Gateway and BFF behavior,
- API versioning strategy,
- Docker-based deployment with one container per service.

## 0) Prerequisites

- Docker Desktop running
- Port 8080 free for gateway
- PowerShell terminal in workspace root

## 1) Start Everything In Separate Containers

Run:

    docker compose up --build -d

Verify each service is isolated in its own container:

    docker compose ps

Expected talking point:

- Each service, BFF, and infra component is an independent deployable unit (separate process and container).

Optional quick health check examples:

    curl http://localhost:8082/api/v1/service-info
    curl http://localhost:8088/api/v1/service-info

Patient service does not expose service-info; use business API for proof:

    curl http://localhost:8081/api/v1/patients/p-001

## 2) Show Boundary Layer As Single Entry Point

Login through gateway:

    curl -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"username":"admin1","password":"secret123"}'

Capture token from accessToken in response.

Call gateway-only public entrypoints (not service ports):

    curl "http://localhost:8080/api/v1/dashboard?patientId=p-001" -H "Authorization: Bearer <TOKEN>"
    curl "http://localhost:8080/api/v1/operations-summary?patientId=p-001" -H "Authorization: Bearer <TOKEN>"

Expected talking point:

- Clients call one edge endpoint (gateway), while internal service topology remains hidden.

## 3) Show BFF API Composition

Call web dashboard endpoint via gateway:

    curl "http://localhost:8080/api/v1/dashboard?patientId=p-001" -H "Authorization: Bearer <TOKEN>" -H "X-Correlation-Id: demo-123"

What to highlight in response payload:

- dashboard includes composed data sections such as patient, appointments, and records.
- this proves request fan-out and aggregation by BFF.

Optional direct comparison (internal endpoint, normally private in production):

    curl "http://localhost:8095/api/v1/dashboard?patientId=p-001" -H "Authorization: Bearer <TOKEN>" -H "X-Correlation-Id: demo-123"

Expected talking point:

- Gateway routes to Web BFF, and Web BFF composes downstream service calls.

## 4) Show Authorization And Policy At Boundary

Get token for partner user:

    curl -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"username":"partner1","password":"secret123"}'

Use partner token against admin-only endpoint (should fail):

    curl "http://localhost:8080/api/v1/operations-summary?patientId=p-001" -H "Authorization: Bearer <PARTNER_TOKEN>"

Expected:

- HTTP 403 Forbidden (gateway route policy and BFF policy enforcement).

Use partner token on partner endpoint (should pass):

    curl "http://localhost:8080/api/v1/partner-overview?patientId=p-001" -H "Authorization: Bearer <PARTNER_TOKEN>"

Expected talking point:

- Boundary layer supports authn/authz before requests reach internal services.

## 5) Show Request Routing, Logging, Metrics, And Rate Limiting

A) Routing proof:

- Call one endpoint per BFF through gateway and show each works.

    curl "http://localhost:8080/api/v1/mobile-overview?patientId=p-001" -H "Authorization: Bearer <TOKEN>"
    curl "http://localhost:8080/api/v1/partner-overview?patientId=p-001" -H "Authorization: Bearer <TOKEN>"

B) Correlation logging proof:

- send a known correlation header, then inspect gateway logs.

    curl "http://localhost:8080/api/v1/dashboard?patientId=p-001" -H "Authorization: Bearer <TOKEN>" -H "X-Correlation-Id: live-demo-corr-001"
    docker compose logs api-gateway --tail=50

Expected:

- log line includes method, path, status, duration, correlationId.

C) Metrics proof:

    curl http://localhost:8080/actuator/metrics
    curl http://localhost:8080/actuator/prometheus

D) Rate limit proof (dashboard route):

PowerShell burst test:

    1..45 | ForEach-Object { curl.exe -s -o NUL -w "%{http_code}`n" "http://localhost:8080/api/v1/dashboard?patientId=p-001" -H "Authorization: Bearer <TOKEN>" }

Expected:

- many 200 responses initially,
- then 429 responses after burst capacity is exceeded.

## 6) Show API Versioning Illustration

Current implementation is v1 endpoints. Demonstrate:

    curl http://localhost:8080/api/v1/dashboard?patientId=p-001 -H "Authorization: Bearer <TOKEN>"
    curl http://localhost:8080/api/v2/dashboard?patientId=p-001 -H "Authorization: Bearer <TOKEN>"

Expected:

- v1 works,
- v2 returns not found (or unmatched route), showing controlled major-version rollout.

Then open strategy docs and explain migration model:

- docs/api-versioning-strategy.md
- services/*/docs/api-versioning-strategy.md

Talking point:

- breaking changes introduce a new major path (/api/v2/...)
- old major can run in parallel during deprecation window.

## 7) Show Aggregated API Discovery At Boundary

Open:

- http://localhost:8080/swagger-ui.html

Highlight:

- one Swagger UI at edge includes docs for gateway, BFFs, and all backend services.
- this reduces client coupling to internal service addresses.

## 8) Clear Demonstration Mapping To Requirements

- Independent microservices: docker compose ps plus direct per-service calls on different ports.
- API communication: BFF aggregate responses show multi-service composition.
- Gateway/BFF behavior: single gateway entry + role-based route controls + BFF aggregation.
- API versioning: v1 active, v2 reserved/documented strategy.
- Docker deployment: one container per service/BFF/gateway with shared infra containers.
- Boundary responsibilities:
  - entry point: gateway on 8080
  - hides complexity: clients do not call internal topology for normal usage
  - routing/composition: gateway routes, BFF composes
  - auth/logging/metrics/caching/rate limit: implemented and observable in live calls

## 9) Demo Teardown

    docker compose down

If you want a clean reset including volumes:

    docker compose down -v
