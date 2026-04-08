Next block name
BLOCK 95 - Recurring Entry Edit Delete Confirmation Accessibility Labels

Objective
Add targeted accessibility semantics for the recurring-entry edit delete confirmation so the dialog exposes a coherent summary and explicit destructive/dismiss action labels without changing the existing delete flow.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreenTest.kt
- app/src/main/res/values/strings.xml
- handoff/validation-report.md

Constraints
- Keep the current delete confirmation behavior and action wiring unchanged
- Prefer explicit accessibility labels and focused semantics over dialog layout changes
- Keep the work limited to the delete confirmation surface and targeted test coverage
- Record the affected validation outcome in `handoff/validation-report.md`

What not to change
- Do not alter recurring-entry form fields, missing-entry recovery, or loading-state behavior
- Do not broaden into dashboard, repository, or navigation refactors
- Do not change the visible confirmation wording unless a small adjacent accessibility string is needed

Done criteria
- The delete confirmation exposes a coherent accessibility summary for assistive technologies
- The destructive and dismiss actions expose explicit accessibility labels if the current defaults are insufficient
- Instrumentation coverage verifies the dialog semantics without regressing existing delete interactions
- The affected validation result is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next block stays localized to dialog semantics and tests but still needs careful Compose accessibility assertions.
