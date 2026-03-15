Next block name
BLOCK 09 - Currency Metadata Startup Sync

Objective
Exercise the new currency metadata foundation by triggering a single sync from the existing app flow, keeping reads local-first and making sync failures non-blocking for the dashboard and recurring-entry experience.

Relevant files
- app/src/main/java/com/example/newfinancetracker/core/app/FinanceTrackerApplication.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardViewModel.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardEffect.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/currency/domain/repository/CurrencyMetadataRepository.kt
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Keep this block focused on invoking the existing currency sync path; do not add a dedicated currency screen
- Preserve the local-first behavior so the app still works when the network request fails
- Treat sync errors as non-fatal and keep recurring-entry data loading unaffected
- Prefer the smallest presentation change that proves the remote slice is being exercised
- Choose the smallest meaningful verification for the updated dashboard/app-start behavior

What not to change
- Do not redesign dashboard sections or recurring-entry CRUD flows
- Do not expand the recurring-entry model to support multi-currency yet
- Do not add periodic background sync, settings, or manual sync controls yet
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The app triggers exactly one currency metadata sync from an existing startup path
- A sync failure does not block the dashboard from showing recurring-entry content
- Any new dashboard state/effect added for this block is small, explicit, and test-covered
- Focused tests verify the startup sync behavior
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The data foundation already exists, so the next step is mostly presentation/lifecycle coordination plus focused testing.
