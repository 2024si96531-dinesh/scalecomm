# PharmacyService API Versioning Strategy

## Service Scope

This strategy applies to PharmacyService endpoints:

- `/api/v1/prescriptions`
- `/api/v1/prescriptions/{prescriptionId}`
- `/api/v1/prescriptions/{prescriptionId}/dispensations`

## Why Versioning Is Needed

Prescription and dispensation APIs can evolve with regulation and workflow requirements. Versioning ensures safe rollout without breaking dependent client behavior.

## Breaking Changes

Examples:

- changing prescription endpoint paths/methods,
- changing required medication fields,
- changing dosage/quantity field types or meaning,
- removing dispensation fields used by consumers,
- non-compatible event payload changes.

Breaking changes require a new major path:

- `/api/v1/prescriptions` -> `/api/v2/prescriptions`

## Non-Breaking Changes And Communication

Examples:

- adding optional prescription metadata,
- additive query/filter support,
- backward-compatible fixes.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish PharmacyService release notes,
- notify BFF and event consumers.

## Semantic Versioning

PharmacyService follows `MAJOR.MINOR.PATCH`.

- MAJOR for breaking changes
- MINOR for backward-compatible additions
- PATCH for backward-compatible bug fixes

Major URL version maps to semver major:

- `1.x.x` -> `/api/v1/...`
- `2.x.x` -> `/api/v2/...`
