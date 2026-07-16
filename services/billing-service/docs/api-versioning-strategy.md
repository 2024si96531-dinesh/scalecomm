# BillingService API Versioning Strategy

## Service Scope

This strategy applies to BillingService endpoints:

- `/api/v1/invoices`
- `/api/v1/invoices/{invoiceId}`
- `/api/v1/payments`
- `/api/v1/payments/{paymentId}`

## Why Versioning Is Needed

Billing contracts are consumed by admin/partner flows and influence financial workflows. Versioning avoids disruption to invoice/payment integrations during contract evolution.

## Breaking Changes

Examples:

- changing invoice/payment endpoint paths or methods,
- changing currency/amount field types or semantics,
- changing required payment fields,
- removing invoice status fields,
- incompatible changes to billing-related events consumed by InsuranceService or NotificationService.

Breaking changes require new major path:

- `/api/v1/invoices` -> `/api/v2/invoices`

## Non-Breaking Changes And Communication

Examples:

- adding optional billing metadata,
- adding additive invoice query filters,
- backward-compatible fixes.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish BillingService release notes,
- notify BFF and consuming services.

## Semantic Versioning

BillingService follows `MAJOR.MINOR.PATCH`.

- MAJOR: breaking contract updates
- MINOR: additive backward-compatible features
- PATCH: backward-compatible fixes

Major path remains aligned with semver major:

- `1.x.x` -> `/api/v1/...`
- `2.x.x` -> `/api/v2/...`
