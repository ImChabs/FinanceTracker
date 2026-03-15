Next block name
BLOCK 08 - Currency Metadata Remote Foundation

Objective
Introduce the first bounded remote-data slice by adding a lightweight currency metadata foundation that fetches from a public API, maps the response into app models, and stores the results locally so the app remains local-first.

Relevant files
- app/build.gradle.kts
- gradle/libs.versions.toml
- app/src/main/java/com/example/newfinancetracker/core/app/FinanceTrackerApplication.kt
- app/src/main/java/com/example/newfinancetracker/core/database/FinanceTrackerDatabase.kt
- app/src/main/java/com/example/newfinancetracker/feature/currency/data/local/
- app/src/main/java/com/example/newfinancetracker/feature/currency/data/remote/
- app/src/main/java/com/example/newfinancetracker/feature/currency/data/repository/
- app/src/main/java/com/example/newfinancetracker/feature/currency/domain/model/
- app/src/main/java/com/example/newfinancetracker/feature/currency/domain/repository/
- app/src/test/java/com/example/newfinancetracker/feature/currency/

Constraints
- Keep this block focused on the remote and local data foundation only; do not build dashboard UI for currencies yet
- Use official stable Android/Kotlin libraries already aligned with the repository stack
- Keep the app usable without a successful network call by persisting fetched results locally
- Prefer a small repository contract and avoid trivial extra abstraction
- Choose the smallest meaningful verification for the added currency data scope

What not to change
- Do not redesign the dashboard or recurring-entry flows
- Do not add currency conversion UI, settings, or manual sync controls yet
- Do not add authentication, backend services, or unrelated infrastructure
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Project dependencies include the minimum networking pieces needed for the chosen remote currency metadata flow
- A new currency feature area exists with domain models, local entities/DAO, remote DTO/service, and repository wiring
- The Room database and application wiring expose the new currency repository without breaking the existing recurring-entry flow
- Focused tests cover at least one meaningful part of the new mapping or repository behavior
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: high
- Rationale: This starts a new cross-layer feature with dependency, database, network, and repository coordination, but it can still stay bounded by avoiding UI work.
