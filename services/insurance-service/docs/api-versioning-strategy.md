# InsuranceService API Versioning Strategy

## Service Scope

This strategy applies to InsuranceService endpoints:

- `/api/v1/policies`
- `/api/v1/policies/{policyId}`
- `/api/v1/claims`
- `/api/v1/claims/{claimId}`

## Why Versioning Is Needed

Policy and claim contracts involve multiple workflows and external integration expectations. Versioning prevents claim lifecycle regressions for current consumers.

## Breaking Changes

Examples:

- changing policy/claim endpoint paths/methods,
- changing required claim submission fields,
- changing coverage/claim amount field types,
- removing claim status fields used by consumers,
- incompatible claim event schema updates.

Breaking changes require a new major path:

- `/api/v1/claims` -> `/api/v2/claims`

## Non-Breaking Changes And Communication

Examples:

- optional policy/claim metadata additions,
- additive read/query capabilities,
- backward-compatible bug fixes.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish InsuranceService changelog,
- notify BFF, BillingService, and other consumers.

## Semantic Versioning

InsuranceService follows `MAJOR.MINOR.PATCH`.

- MAJOR for breaking changes
- MINOR for backward-compatible features
- PATCH for backward-compatible fixes

Path major aligns with semver major:

- `1.x.x` -> `/api/v1/...`
- `2.x.x` -> `/api/v2/...`
