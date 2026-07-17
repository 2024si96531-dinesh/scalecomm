# AppointmentService API Versioning Strategy

## Service Scope

This strategy applies to AppointmentService endpoints:

- `/api/v1/appointments`
- `/api/v1/appointments/{appointmentId}`
- `/api/v1/appointments/{appointmentId}/reschedule`
- `/api/v1/appointments/{appointmentId}/cancel`

## Why Versioning Is Needed

Appointment APIs are consumed by BFFs and trigger async workflows with BillingService and NotificationService. Versioning ensures lifecycle changes do not disrupt booking integrations.

## Breaking Changes

Examples:

- changing appointment lifecycle endpoint paths or methods,
- changing required booking fields,
- changing date/time formats in request/response,
- removing status values that current clients depend on,
- incompatible changes in appointment event payload schema.

Breaking changes require new major path:

- `/api/v1/appointments` -> `/api/v2/appointments`

## Non-Breaking Changes And Communication

Examples:

- adding optional fields,
- adding additional filter/query options,
- additive status metadata without removing existing fields.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish AppointmentService release notes,
- notify BFF and event consumer teams.

## Semantic Versioning

AppointmentService follows `MAJOR.MINOR.PATCH`.

- MAJOR: breaking changes (`1.x.x` -> `2.0.0`)
- MINOR: backward-compatible features (`1.3.0` -> `1.4.0`)
- PATCH: fixes (`1.3.0` -> `1.3.1`)

Path major maps to semver major:

- `1.x.x` uses `/api/v1/...`
- `2.x.x` uses `/api/v2/...`
