# NotificationService API Versioning Strategy

## Service Scope

This strategy applies to NotificationService endpoints:

- `/api/v1/notifications`
- `/api/v1/notifications/{notificationId}`
- `/api/v1/notifications/{notificationId}/status`

## Why Versioning Is Needed

NotificationService receives events from multiple services and serves client status checks. Versioning prevents breakage in delivery status consumption and event-driven workflows.

## Breaking Changes

Examples:

- changing notification endpoint paths/methods,
- changing required notification request fields,
- changing delivery status schema or type values incompatibly,
- removing fields used by BFF consumers,
- incompatible event payload contract updates.

Breaking changes require a new major path:

- `/api/v1/notifications` -> `/api/v2/notifications`

## Non-Breaking Changes And Communication

Examples:

- adding optional notification metadata,
- additive status detail fields,
- backward-compatible fixes.

Communication method:

- update OpenAPI (`/v3/api-docs`),
- publish NotificationService release notes,
- notify all event producers and BFF teams.

## Semantic Versioning

NotificationService follows `MAJOR.MINOR.PATCH`.

- MAJOR for breaking changes
- MINOR for backward-compatible additions
- PATCH for backward-compatible fixes

Path major maps to semver major:

- `1.x.x` -> `/api/v1/...`
- `2.x.x` -> `/api/v2/...`
