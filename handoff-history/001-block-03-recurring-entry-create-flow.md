Next block name
BLOCK 03 - Recurring Entry Create Flow

Objective
Implement the first recurring-entry CRUD slice by adding a create screen, form state management, basic validation, and persistence through the new local repository foundation.

Relevant files
- app/src/main/java/com/example/newfinancetracker/core/navigation/FinanceTrackerNavHost.kt
- app/src/main/java/com/example/newfinancetracker/core/navigation/FinanceTrackerDestination.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/domain/repository/RecurringEntryRepository.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/data/repository/OfflineRecurringEntryRepository.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/

Constraints
- Keep this block focused on create-only behavior for recurring entries
- Follow the screen convention: use `XScreen` + `XScreenRoot` if a ViewModel is introduced
- Use `StateFlow` for UI state and `SharedFlow` for one-off effects if needed
- Reuse the repository and Room foundation from Block 02 instead of adding parallel persistence paths
- Prefer the smallest meaningful verification for the affected `app` scope

What not to change
- Do not implement edit or delete flows yet
- Do not add remote/API work
- Do not redesign the dashboard beyond the minimum navigation entry point needed for the create flow
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- A recurring-entry create screen exists with `Screen` and `ScreenRoot`
- Form state, actions, and ViewModel logic are separated from composables
- Basic validation prevents saving invalid entries
- Saving persists a new recurring entry through the repository
- Navigation into and out of the create flow works
- The smallest meaningful verification is attempted
