# PatientService API Versioning Strategy

## Service Scope

This strategy applies to PatientService endpoints:

- `/api/v1/patients`
- `/api/v1/patients/{patientId}`

## Why Versioning Is Needed

Patient data is consumed by multiple services and BFFs. Versioning prevents downstream breakage when patient contract changes are introduced and allows phased migration.

## Breaking Changes

A change is breaking if existing clients cannot continue without modification.

Examples:

- removing or renaming patient endpoints,
- changing request/response field names,
- changing field types (for example, `dateOfBirth` format/type),
- making optional patient fields mandatory,
- removing fields used by consumers.

Breaking changes require a new major API path:

- `/api/v1/patients` -> `/api/v2/patients`

## Non-Breaking Changes And Communication

Examples:

- adding optional fields,
- adding new patient search/filter endpoints,
- bug fixes without contract break.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish PatientService changelog entry,
- notify BFF owners and dependent service teams.

## Semantic Versioning

PatientService releases follow `MAJOR.MINOR.PATCH`.

- MAJOR: breaking API changes (for example, `1.6.0` -> `2.0.0`)
- MINOR: backward-compatible features (`1.6.0` -> `1.7.0`)
- PATCH: backward-compatible fixes (`1.6.0` -> `1.6.1`)

Path major aligns with semver major:

- semver `1.x.x` -> `/api/v1/...`
- semver `2.x.x` -> `/api/v2/...`
