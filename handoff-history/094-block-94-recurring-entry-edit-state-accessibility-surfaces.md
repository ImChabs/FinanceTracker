Next block name
BLOCK 94 - Recurring Entry Edit State Accessibility Surfaces

Objective
Add merged accessibility coverage for the recurring-entry edit loading and missing-entry states so those screens expose coherent summaries and state descriptions instead of relying only on visible copy.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreenTest.kt
- app/src/main/res/values/strings.xml
- handoff/validation-report.md

Constraints
- Keep the existing edit navigation and missing-entry recovery flow unchanged
- Prefer merged semantics and targeted accessibility copy over visual layout changes
- Keep the work limited to loading and missing-entry state surfaces plus focused test coverage
- Record the affected validation outcome in `handoff/validation-report.md`

What not to change
- Do not alter recurring-entry form fields, validation rules, or delete behavior
- Do not broaden into dashboard, repository, or navigation refactors
- Do not change the current visible wording unless accessibility semantics require a small adjacent copy addition

Done criteria
- The edit loading state exposes a coherent accessibility surface beyond the visible spinner and text
- The missing-entry state exposes a merged accessibility summary and any needed state/action labels
- Instrumentation coverage verifies the new semantics without regressing existing edit interactions
- The affected validation result is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next block stays localized to edit-state semantics but needs careful Compose accessibility assertions and focused validation.
