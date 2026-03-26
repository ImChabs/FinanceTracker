Next block name
BLOCK 92 - Dashboard Inactive Entry Interaction Coverage

Objective
Add focused coverage that proves inactive recurring entries remain reachable from the dashboard list and still navigate into edit flows after being saved.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardViewModel.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardViewModelTest.kt
- handoff/validation-report.md

Constraints
- Keep active-only totals and upcoming-payments behavior unchanged
- Cover only inactive-entry visibility and interaction from the saved entries list
- Prefer focused dashboard tests over broader navigation or repository changes
- Record the affected validation outcome in `handoff/validation-report.md`

What not to change
- Do not broaden into recurring-entry form behavior
- Do not change persistence ordering or data schema
- Do not rework dashboard summary calculations

Done criteria
- A test proves an inactive saved entry is rendered in the dashboard saved-entries section
- A test proves selecting that inactive entry dispatches the expected edit/navigation action
- The affected validation result is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step is still localized, but interaction coverage across dashboard state and UI assertions needs a careful pass.
