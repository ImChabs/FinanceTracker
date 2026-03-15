Next block name
BLOCK 04 - Dashboard Recurring Overview

Objective
Connect the dashboard to local recurring-entry data so saved items become visible through a read-only overview, including an empty state, a recurring-entry list, and a monthly recurring total summary.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/main/java/com/example/newfinancetracker/feature/recurring/domain/repository/RecurringEntryRepository.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/domain/model/RecurringEntry.kt
- app/src/main/java/com/example/newfinancetracker/core/navigation/FinanceTrackerNavHost.kt

Constraints
- Keep this block focused on read-only dashboard consumption of recurring-entry data
- Introduce `DashboardScreenRoot` plus a ViewModel if repository observation is needed
- Use `StateFlow` for dashboard UI state and keep data shaping out of composables
- Reuse the existing local repository flow instead of adding a parallel data path
- Prefer the smallest meaningful verification for the affected `app` scope

What not to change
- Do not implement recurring-entry edit or delete flows yet
- Do not redesign the recurring-entry create form beyond small required fixes
- Do not add remote/API work
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The dashboard observes recurring entries from the repository
- The dashboard shows a meaningful empty state when there are no entries
- Saved recurring entries are listed with key details
- A monthly recurring total summary is derived and displayed on the dashboard
- Navigation to the create flow still works after the dashboard changes
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: high
- Rationale: This block adds reactive dashboard state plus summary calculations, so it needs careful UI-state shaping without spilling business logic into composables.
