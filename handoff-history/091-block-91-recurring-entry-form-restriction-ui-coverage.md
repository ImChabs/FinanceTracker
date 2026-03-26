Next block name
BLOCK 91 - Recurring Entry Form Restriction UI Coverage

Objective
Add focused screen-level coverage for the recurring-entry create/edit form so the removed category field and restricted USD/PYG currency selector are exercised through the UI.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/form/RecurringEntryForm.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/create/RecurringEntryCreateScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the current recurring-entry persistence model unchanged unless testability absolutely requires a tiny adjacent fix
- Cover only the category removal and USD/PYG currency restriction behaviors
- Prefer focused Compose UI tests over broader integration work
- Use the smallest meaningful affected-scope verification and record it in `handoff/validation-report.md`

What not to change
- Do not reintroduce category input or validation
- Do not broaden into dashboard behavior or repository/data-layer cleanup
- Do not expand the allowed currency list beyond USD and PYG

Done criteria
- A UI test proves the create/edit form no longer renders a category field
- A UI test proves the currency selector exposes only USD and PYG
- The affected validation result is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step is localized but UI assertions around form fields and dropdown content usually need a bit of careful Compose test setup.
