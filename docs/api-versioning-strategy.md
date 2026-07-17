# API Versioning Strategy

## Service-Specific Strategy Documents

Use the following files for per-service API versioning details:

- `services/patient-service/docs/api-versioning-strategy.md`
- `services/appointment-service/docs/api-versioning-strategy.md`
- `services/health-record-service/docs/api-versioning-strategy.md`
- `services/billing-service/docs/api-versioning-strategy.md`
- `services/auth-service/docs/api-versioning-strategy.md`
- `services/pharmacy-service/docs/api-versioning-strategy.md`
- `services/insurance-service/docs/api-versioning-strategy.md`
- `services/notification-service/docs/api-versioning-strategy.md`

## Purpose

This document defines a consistent API versioning approach for all backend services in this healthcare microservices system.

Covered services:

- PatientService
- AppointmentService
- HealthRecordService
- BillingService
- AuthService
- PharmacyService
- InsuranceService
- NotificationService

## Why API Versioning Is Needed

API versioning is required because services and client applications evolve at different speeds. In this system, the same APIs are consumed by multiple BFFs (web, mobile, admin, partner) and potentially by external partners.

Without versioning, a change made for one consumer can break others unexpectedly. Versioning allows us to:

- preserve backward compatibility for existing consumers,
- roll out new capabilities safely,
- support controlled migration from old contracts to new contracts,
- reduce deployment risk in independently released microservices.

## Versioning Method

### REST API path versioning

All public REST endpoints are versioned in the URL path:

- `.../api/v1/...`
- `.../api/v2/...` (introduced only when required by breaking changes)

This is simple to document, easy for BFF routing, and explicit for consumers.

### Event contract versioning

For asynchronous events, version is included in the event metadata and/or event type naming convention.

Examples:

- event type: `appointment.booked.v1`
- header/field: `eventVersion: 1`

Consumers must process only compatible versions and fail fast for unknown major versions.

## What Counts As A Breaking Change

A change is **breaking** if an existing client built against the current API can no longer work without modification.

Breaking changes include:

- removing an endpoint,
- changing endpoint path or HTTP method,
- removing a response field that clients may rely on,
- renaming request/response fields,
- changing data type or format (for example, `string` to `number`, or date format change),
- making an optional field mandatory,
- changing validation rules in a way that previously valid requests now fail,
- changing error response schema/status behavior in a way that breaks client handling,
- changing event payload fields/types in non-backward-compatible ways.

When any breaking change is introduced, we publish a new **major** API version (for example, `v1` to `v2`).

## Non-Breaking Changes And Communication

A change is non-breaking if existing clients continue to work without modification.

Typical non-breaking changes:

- adding new endpoints,
- adding optional request fields,
- adding response fields (clients should ignore unknown fields),
- performance improvements,
- bug fixes that do not change contract behavior,
- documentation clarifications.

How non-breaking changes are communicated:

- update OpenAPI spec (`/v3/api-docs`) for each service,
- publish release notes/changelog entry per service,
- announce changes to BFF and consumer teams,
- keep API examples updated in service documentation.

## Semantic Versioning Policy

Each service API follows Semantic Versioning: `MAJOR.MINOR.PATCH`.

- **MAJOR**: increment for breaking changes.
  Example: `1.4.2` -> `2.0.0`
- **MINOR**: increment for backward-compatible feature additions.
  Example: `1.4.2` -> `1.5.0`
- **PATCH**: increment for backward-compatible bug fixes.
  Example: `1.4.2` -> `1.4.3`

### Relationship Between URL Version And SemVer

- URL path major (`/api/v1`, `/api/v2`) maps to SemVer major.
- MINOR and PATCH updates do not change URL path major.
- Service artifacts (Docker image tags and release versions) carry full SemVer.

Example:

- REST URL remains `/api/v1/patients`
- service release evolves `1.2.0` -> `1.3.0` -> `1.3.1`

Only when a breaking change is introduced:

- new URL `/api/v2/patients`
- service release becomes `2.0.0`

## Service-Wise Application

The same strategy is applied consistently to every service:

1. PatientService
- Base path: `/api/v1/patients`
- Breaking contract changes introduce `/api/v2/patients`

2. AppointmentService
- Base path: `/api/v1/appointments`
- Breaking contract changes introduce `/api/v2/appointments`

3. HealthRecordService
- Base path: `/api/v1/records` and `/api/v1/patients/{patientId}/records`
- Breaking contract changes introduce `/api/v2/...`

4. BillingService
- Base path: `/api/v1/invoices`, `/api/v1/payments`
- Breaking contract changes introduce `/api/v2/...`

5. AuthService
- Base path: `/api/v1/users`, `/api/v1/roles`
- Breaking contract changes introduce `/api/v2/...`

6. PharmacyService
- Base path: `/api/v1/prescriptions`
- Breaking contract changes introduce `/api/v2/...`

7. InsuranceService
- Base path: `/api/v1/policies`, `/api/v1/claims`
- Breaking contract changes introduce `/api/v2/...`

8. NotificationService
- Base path: `/api/v1/notifications`
- Breaking contract changes introduce `/api/v2/...`

## Deprecation And Support Window

To avoid abrupt consumer impact, old major versions are deprecated in phases:

- Mark deprecated endpoints in OpenAPI and docs.
- Provide migration notes from old major to new major.
- Run both major versions in parallel for a defined window (for example, 6 months).
- Remove old version only after communicated end-of-support date.

## Consumer Expectations

All API consumers (BFFs and external clients) must:

- pin to a known major API version,
- tolerate additive (non-breaking) response fields,
- monitor changelogs and deprecation notices,
- plan migration during deprecation windows.

## Summary

This strategy provides a clear, predictable contract evolution model for all services:

- explicit URL major versioning for REST,
- contract-safe handling of asynchronous event versions,
- strict breaking-change discipline,
- transparent communication for non-breaking evolution,
- Semantic Versioning for release management.
