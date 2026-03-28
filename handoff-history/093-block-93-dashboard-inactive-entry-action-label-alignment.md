Next block name
BLOCK 93 - Dashboard Inactive Entry Action Label Alignment

Objective
Align the saved-entry card accessibility click label with inactive recurring entries so dashboard interactions do not describe paused entries as active while preserving existing edit navigation behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- app/src/main/res/values/strings.xml
- handoff/validation-report.md

Constraints
- Keep inactive saved entries visible in the dashboard saved-entries list
- Keep recurring-entry click behavior routed to the existing edit flow
- Prefer a focused accessibility-copy change over broader dashboard restructuring
- Record the affected validation outcome in `handoff/validation-report.md`

What not to change
- Do not alter dashboard summary totals or upcoming-payments filtering
- Do not change recurring-entry persistence or edit-screen behavior
- Do not broaden into new navigation flows or repository work

Done criteria
- Inactive saved-entry cards expose an accessibility click label that does not claim the entry is active
- Active saved-entry cards keep a correct edit action label
- Dashboard interaction coverage is updated for the adjusted copy
- The affected validation result is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step is localized but needs careful accessibility-copy alignment without regressing existing saved-entry interaction coverage.
