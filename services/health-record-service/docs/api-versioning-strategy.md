# HealthRecordService API Versioning Strategy

## Service Scope

This strategy applies to HealthRecordService endpoints:

- `/api/v1/records`
- `/api/v1/records/{recordId}`
- `/api/v1/patients/{patientId}/records`

## Why Versioning Is Needed

Clinical data contracts are sensitive and long-lived. Versioning protects integrations when medical record schemas evolve and helps preserve compatibility with existing consumers.

## Breaking Changes

Examples:

- removing/renaming record endpoints,
- changing mandatory clinical payload fields,
- changing data types/format for core medical attributes,
- removing response fields used by dependent components.

Breaking changes require a new major path:

- `/api/v1/records` -> `/api/v2/records`

## Non-Breaking Changes And Communication

Examples:

- adding optional clinical metadata,
- adding new read endpoints,
- bug fixes that keep contract stable.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish HealthRecordService changelog,
- communicate to BFF/dependent teams.

## Semantic Versioning

HealthRecordService follows `MAJOR.MINOR.PATCH`.

- MAJOR for breaking changes
- MINOR for backward-compatible additions
- PATCH for backward-compatible bug fixes

Path major and semver major stay aligned:

- `1.x.x` with `/api/v1/...`
- `2.x.x` with `/api/v2/...`
