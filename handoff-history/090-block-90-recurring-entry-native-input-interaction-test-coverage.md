Next block name
BLOCK 90 - Recurring Entry Native Input Interaction Test Coverage

Objective
Add focused UI coverage for the recurring-entry create/edit form so the new native amount formatting and date-selection interactions are exercised at the screen level.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/form/RecurringEntryForm.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/create/RecurringEntryCreateScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the current create/edit viewmodel contracts and repository behavior unchanged
- Prefer screen-level Compose UI tests over broader integration work
- Cover the native input behaviors without introducing a custom calendar abstraction just for tests
- Use the smallest meaningful affected-scope verification and record it in `handoff/validation-report.md`

What not to change
- Do not redesign the recurring-entry form layout
- Do not broaden into dashboard coverage or unrelated recurring-entry validation changes
- Do not replace the Material 3 date picker approach introduced in this block

Done criteria
- A focused UI test proves the amount field renders grouped separators during interaction without breaking submission
- A focused UI test proves date selection updates the visible form value for at least one recurring-entry flow
- The affected validation result is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step is localized but UI-test interaction with formatted text fields and Material 3 date pickers usually needs a bit of careful test setup.
