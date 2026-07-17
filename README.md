# Healthcare Microservices Workspace

This workspace bootstraps a Spring Boot healthcare microservices system with independent service folders that are intended to be maintained as separate repositories.

Current implementation goals:

- keep each microservice independently buildable and deployable
- keep each service database isolated using the database-per-service pattern
- expose synchronous APIs over REST
- support low-coupled workflows through asynchronous event publication and consumption
- introduce dedicated BFFs for web, mobile, admin, and partner clients
- centralize ingress concerns (authentication, authorization, logging, metrics, and rate limiting) via API Gateway

Initial assets in this workspace:

- architecture and application documents under `docs/`
- MySQL schema and table creation scripts under `sql/mysql/`
- runnable Spring Boot scaffolds under `services/` and `bff/`
- shared Docker and compose support under `docker/` and `compose.yaml`
- local dev-run script under `scripts/`

Implemented backend services:

- `patient-service`: patient CRUD, OpenAPI, RabbitMQ event publishing, CQRS-style command/query split
- `appointment-service`: appointment CRUD, OpenAPI, RabbitMQ event publishing, CQRS-style command/query split
- `health-record-service`: health record CRUD and OpenAPI
- `billing-service`: invoice CRUD, appointment event consumption, invoice event publishing, OpenAPI, CQRS-style command/query split
- `auth-service`: user CRUD, login JWT issuance, and OpenAPI
- `pharmacy-service`: prescription CRUD and OpenAPI
- `insurance-service`: policy CRUD and OpenAPI
- `notification-service`: notification CRUD, domain event consumption, OpenAPI, CQRS-style command/query split

Implemented BFFs:

- `web-bff`: dashboard aggregation over patient, appointment, and record services
- `mobile-bff`: mobile overview aggregation over patient, appointment, and pharmacy services
- `admin-bff`: operational summary aggregation over billing, insurance, auth, and notification services
- `partner-bff`: partner overview aggregation over insurance, appointment, and billing services

Implemented edge service:

- `api-gateway`: single public entry with JWT validation, route-level authorization, rate limiting, correlation logging, and aggregated Swagger UI

API gateway guide:

- `docs/api-gateway.md`

Security hardening now in place:

- gateway route-level role authorization for BFF entrypoints
- BFF method-level authorization and header relay (`Authorization`, `X-Correlation-Id`) to downstream services
- downstream role-based JWT authorization in `billing-service` and `insurance-service`

## OpenAPI

Each service and BFF exposes generated OpenAPI docs at:

- `/v3/api-docs`
- `/swagger-ui.html`

Use the gateway aggregated Swagger UI for one-stop exploration:

- `http://localhost:8080/swagger-ui.html`

Gateway routes each downstream OpenAPI doc under `/v3/api-docs/{service-name}`.

## Local Development

Start infrastructure and all applications with Docker Compose:

```powershell
docker compose up --build
```

Gateway public base URL:

- `http://localhost:8080`

## Authentication Quickstart

Login to receive JWT token:

```powershell
curl -X POST http://localhost:8080/api/v1/auth/login \
	-H "Content-Type: application/json" \
	-d '{"username":"admin1","password":"secret123"}'
```

Use token for secured API calls:

```powershell
curl "http://localhost:8080/api/v1/operations-summary?patientId=p-001" \
	-H "Authorization: Bearer <accessToken>"
```

## Seed Sample Data

Compose automatically loads SQL scripts from `sql/mysql` in lexical order, including sample records from:

- `sql/mysql/03-seed-sample-data.sql`

Seeded demo users:

- `admin1 / secret123` (ADMIN + DOCTOR)
- `partner1 / secret123` (PARTNER)
- `patient1 / secret123` (PATIENT)

Seeded business data includes:

- patients, appointments, health records
- invoices, payments
- policies, claims
- prescriptions, dispensations
- notifications, delivery attempts

Run a single module locally with Maven:

```powershell
./scripts/dev-run.ps1 services/patient-service
./scripts/dev-run.ps1 bff/web-bff
./scripts/dev-run.ps1 gateway/api-gateway
```

## Event Workflow

The first asynchronous workflow is implemented as an event-driven chain:

1. `AppointmentService` publishes `appointment.booked`
2. `BillingService` consumes `appointment.booked` and creates an invoice
3. `BillingService` publishes `invoice.issued`
4. `NotificationService` consumes domain events and creates outbound notification records