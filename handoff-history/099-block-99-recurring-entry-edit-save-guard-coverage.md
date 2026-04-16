Next block name
BLOCK 99 - Recurring Entry Edit Save Guard Coverage

Objective
Add focused `RecurringEntryEditViewModel` coverage for save guard behavior so save actions stop cleanly when the edit state does not allow submission.

Relevant files
- app/src/test/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditViewModelTest.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditViewModel.kt
- handoff/validation-report.md

Constraints
- Keep the current save workflow and `canSubmit` logic unchanged unless a small test-enabling adjustment is required
- Prefer unit tests that exercise existing state transitions over screen or navigation changes
- Keep verification focused on the affected unit-test path
- Record the affected validation outcome in `handoff/validation-report.md`

What not to change
- Do not alter repository contracts, save side effects, or navigation behavior
- Do not broaden into delete workflow, currency handling, or dashboard work
- Do not refactor the ViewModel beyond what is required for localized coverage

Done criteria
- Targeted coverage verifies `SaveClicked` leaves state coherent when submission is unavailable
- Targeted coverage verifies `SaveClicked` does not call the repository when `canSubmit` is false
- Any required test helper stays localized to recurring-entry edit ViewModel coverage
- The affected validation result is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The work stays in one ViewModel test file, but it needs deliberate state setup to prove the submit guard without changing production behavior.
