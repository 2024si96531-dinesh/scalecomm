**Plan: Healthcare Microservices Prompt**

Create one greenfield, one-shot implementation prompt for a Spring Boot healthcare microservices system that includes only the scope you confirmed: `PatientService`, `AppointmentService`, `HealthRecordService`, `BillingService`, `AuthService`, `PharmacyService`, `InsuranceService`, and `NotificationService`. The prompt will require independent repositories, database-per-service ownership, REST for synchronous communication, asynchronous messaging for low coupling, BFF support for web, mobile, admin, and partner clients, and URI-based API versioning with semantic release versioning.

**Steps**
1. Fix the implementation scope to exactly the eight services and four client types you named, with no additional business modules.
2. Structure the prompt so the implementation agent must first define each service’s bounded responsibility, public API, and owned data before generating code.
3. Require every service to be independently buildable, testable, deployable, and maintained in its own repository with no shared database schema.
4. Require explicit collaboration mapping across services, with each interaction labeled as `command`, `query`, or `event`.
5. Require REST for synchronous service calls and asynchronous messaging for decoupled workflows; do not allow the prompt to silently introduce GraphQL or gRPC.
6. Require BFF coverage for the four client types you selected, while allowing the implementation agent to choose one BFF layer or multiple BFFs only for those clients.
7. Require the prompt to apply `CQRS`, `Saga`, and `Event Sourcing` selectively and justify where they fit instead of forcing them everywhere.
8. Require URI versioning such as `/api/v1/...` and semantic versioning rules for `major`, `minor`, and `patch` releases.
9. Add verification expectations so the generated system proves service independence, contract correctness, event-flow correctness, and deployability.
10. Present the final implementation prompt for approval before any code generation begins.

**Verification**
1. Confirm the prompt names exactly eight services and exactly four client types.
2. Confirm each service is described as independently owned, tested, deployed, and backed by its own datastore.
3. Confirm collaboration rules explicitly distinguish `command`, `query`, and `event` interactions.
4. Confirm the prompt mandates `REST + async messaging` and does not add other protocols.
5. Confirm the prompt includes both URI API versioning and semantic release versioning.
6. Confirm `CQRS`, `Saga`, and `Event Sourcing` are framed as justified choices, not blanket requirements.

**Decisions**
- Included service scope is limited to the eight services you listed.
- Included client scope is limited to web portal, mobile app, admin portal, and partner portal.
- Technology versions remain generic; the prompt will not pin Java or Spring Boot versions.
- Out of scope: extra healthcare domains, cloud/vendor assumptions, concrete broker/database product choices, and frontend implementation details.

If this plan matches your intent, approve it and I’ll convert it into the final one-shot implementation prompt.

**Database Schema Creation**
Use below MySQL connection parameters to create db schemas for each service and relevant tables.
Hostname : 127.0.0.1
Port : 3306
User: root
Password: Compas@123

Also create the briefy application description document, architecture diagram and mermaid diagram for the microservices system, showing the eight services, their interactions (commands, queries, events), and the BFF layers for the four client types. The diagram should clearly indicate the independent repositories and databases for each service, as well as the communication protocols used (REST for synchronous calls and asynchronous messaging for decoupled workflows).