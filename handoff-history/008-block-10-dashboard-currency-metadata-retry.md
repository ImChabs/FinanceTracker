Next block name
BLOCK 10 - Dashboard Currency Metadata Retry

Objective
Add a small dashboard-driven retry path for currency metadata refresh so the user can re-attempt the remote sync after a startup failure without affecting recurring-entry visibility or introducing periodic/background sync behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardAction.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardViewModel.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardEffect.kt
- app/src/main/res/values/strings.xml
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Keep the retry entry point inside the existing dashboard summary/status area; do not add a dedicated currency screen
- Reuse the existing repository sync API instead of introducing a new data layer abstraction
- Preserve local-first behavior so cached currency metadata remains visible while a retry is running or fails
- Keep recurring-entry list and upcoming-payment loading unaffected by retry attempts
- Choose the smallest meaningful verification for the dashboard retry flow

What not to change
- Do not add periodic background sync, alarms, workers, or settings toggles
- Do not expand recurring entries to store a selected currency yet
- Do not redesign the dashboard layout beyond the small retry affordance needed for this block
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The dashboard exposes a small retry action when currency metadata is unavailable or refresh failed
- Retrying invokes the existing currency metadata sync path without creating duplicate startup sync logic
- Retry progress and failure/success outcome are explicit in dashboard state/effects and test-covered
- Recurring-entry content remains usable before, during, and after retry attempts
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step stays within the dashboard layer, but it needs careful UI-state coordination to keep retry behavior explicit and non-blocking.
