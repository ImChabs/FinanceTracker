Next block name
BLOCK 02 — Local Recurring Entry Data Foundation

Objective
Introduce the first local data-layer foundation for recurring entries so the dashboard and future CRUD screens can build on stable models and persistence contracts.

Relevant files
- app/src/main/java/com/example/newfinancetracker/core/app/FinanceTrackerApp.kt
- app/src/main/java/com/example/newfinancetracker/core/navigation/FinanceTrackerNavHost.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/build.gradle.kts
- gradle/libs.versions.toml

Constraints
- Add only the minimum local data foundation needed for recurring entries
- Follow the repository layer rules: `domain` contracts separated from `data` implementations
- Keep the dashboard placeholder visually intact unless a small wiring change is required
- Prefer focused verification for the `app` module or affected source set

What not to change
- Do not add remote/API work
- Do not add charts, reminders, settings, or extra screens
- Do not implement full CRUD UI flows yet
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- A recurring entry domain model exists
- Room foundation is introduced for recurring entries
- DAO and local entity mapping are in place
- A repository contract and initial implementation exist
- The smallest meaningful verification is attempted
