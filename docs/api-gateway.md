# API Gateway Guide

## Purpose

The API gateway is the single public ingress for this workspace. It centralizes:

- JWT authentication and token validation
- route-level authorization
- Redis-backed rate limiting
- request correlation logging
- aggregated Swagger UI and OpenAPI discovery

## Public Endpoints

- Login: `POST /api/v1/auth/login`
- Web dashboard: `GET /api/v1/dashboard`
- Mobile overview: `GET /api/v1/mobile-overview`
- Admin operations summary: `GET /api/v1/operations-summary`
- Partner overview: `GET /api/v1/partner-overview`

## Role Policy

- `ADMIN`: access to all gateway BFF routes and user management routes
- `PARTNER`: access to partner overview
- `PATIENT`, `DOCTOR`: access to dashboard and mobile overview

## Downstream Security Posture

- BFF-to-service calls forward `Authorization` and `X-Correlation-Id` headers.
- `billing-service` and `insurance-service` now enforce JWT resource-server validation and role checks.
- Effective policy is defense in depth:
	- gateway route authorization gates coarse access
	- BFF method-level authorization gates aggregator entrypoints
	- downstream service authorization protects direct service access paths

## Rate Limits (Initial)

- `dashboard`: replenish 20, burst 40
- `mobile-overview`: replenish 20, burst 40
- `operations-summary`: replenish 10, burst 20
- `partner-overview`: replenish 15, burst 30

Rate-limit key strategy:

- authenticated user key: `user:{jwt-sub}`
- fallback key: client remote address

## Swagger Aggregation

Gateway Swagger UI:

- `http://localhost:8080/swagger-ui.html`

Aggregated docs paths:

- `/v3/api-docs`
- `/v3/api-docs/auth-service`
- `/v3/api-docs/web-bff`
- `/v3/api-docs/mobile-bff`
- `/v3/api-docs/admin-bff`
- `/v3/api-docs/partner-bff`
- `/v3/api-docs/patient-service`
- `/v3/api-docs/appointment-service`
- `/v3/api-docs/health-record-service`
- `/v3/api-docs/billing-service`
- `/v3/api-docs/pharmacy-service`
- `/v3/api-docs/insurance-service`
- `/v3/api-docs/notification-service`

## Docker Compose Notes

Compose starts Redis for gateway rate limiting and exports a shared JWT secret to gateway, auth-service, and BFFs.

## Demo Walkthrough

For a live demonstration that covers independent services, gateway/BFF boundary behavior, API communication, versioning illustration, and container deployment, use:

- `docs/demo-runbook.md`

For production:

- replace default JWT secret with secure secret management
- restrict direct external exposure of BFF/service ports
- keep gateway as the only public endpoint
