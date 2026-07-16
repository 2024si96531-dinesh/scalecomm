# AuthService API Versioning Strategy

## Service Scope

This strategy applies to AuthService endpoints:

- `/api/v1/users`
- `/api/v1/users/{userId}`
- `/api/v1/users/{userId}/roles`
- `/api/v1/roles`

## Why Versioning Is Needed

AuthService contracts affect access control across all clients. Versioning ensures identity and role model changes are introduced safely.

## Breaking Changes

Examples:

- changing user/role endpoint paths or methods,
- changing required identity fields,
- changing role assignment request/response schema,
- removing fields relied on by BFF authorization logic.

Breaking changes require new major path:

- `/api/v1/users` -> `/api/v2/users`

## Non-Breaking Changes And Communication

Examples:

- adding optional user metadata fields,
- adding non-mandatory role attributes,
- patch fixes that keep request/response compatibility.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish AuthService changelog,
- notify all BFF teams due to auth impact.

## Semantic Versioning

AuthService follows `MAJOR.MINOR.PATCH`.

- MAJOR: breaking changes
- MINOR: backward-compatible features
- PATCH: backward-compatible fixes

Major path matches semver major:

- `1.x.x` -> `/api/v1/...`
- `2.x.x` -> `/api/v2/...`
