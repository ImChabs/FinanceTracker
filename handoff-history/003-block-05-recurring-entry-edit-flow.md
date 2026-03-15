Next block name
BLOCK 05 - Recurring Entry Edit Flow

Objective
Allow the user to open an existing recurring entry from the dashboard, load its saved data into an edit form, and persist updates back through the local repository.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/core/navigation/FinanceTrackerNavHost.kt
- app/src/main/java/com/example/newfinancetracker/core/navigation/FinanceTrackerDestination.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/domain/repository/RecurringEntryRepository.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/create/
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/

Constraints
- Keep this block focused on editing an existing recurring entry that is selected from the dashboard
- Introduce `RecurringEntryEditScreen` plus `RecurringEntryEditScreenRoot` and ViewModel-backed state if a separate edit flow is created
- Reuse the existing local repository methods for loading and saving entries instead of adding a parallel persistence path
- Preserve the current dashboard overview and recurring-entry create flow while adding edit navigation
- Prefer the smallest meaningful verification for the affected `app` scope

What not to change
- Do not implement delete flow yet
- Do not redesign the dashboard overview beyond the minimum tap target and navigation needed for editing
- Do not add remote/API work
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- A saved dashboard entry can be selected to open an edit flow
- The edit screen loads the selected entry's current values
- Saving persists updates through the repository and returns to the previous screen
- The create flow still works after the edit flow changes
- A missing-entry path is handled reasonably
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: high
- Rationale: This block adds navigation arguments plus loaded form state, and it may require careful reuse of the existing create-form structure without introducing awkward duplication.
