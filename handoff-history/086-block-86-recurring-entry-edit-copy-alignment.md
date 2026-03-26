Next block name
BLOCK 86 - Recurring Entry Edit Copy Alignment

Objective
Align the recurring-entry edit flow copy with the neutral recurring-entry wording now used elsewhere so loading, missing-entry, and delete-confirmation messages no longer describe the item as a saved entry.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreen.kt
- app/src/main/res/values/strings.xml
- app/src/test/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditViewModelTest.kt
- handoff/validation-report.md

Constraints
- Keep recurring-entry edit navigation, loading behavior, and delete behavior unchanged
- Limit the block to copy alignment for the existing edit states and confirmation content; do not broaden into dashboard or create-flow wording
- Update tests only where expectations depend on the adjusted copy
- Use the smallest meaningful verification for the touched edit-flow scope and record the result in `handoff/validation-report.md`

What not to change
- Do not change repository contracts, edit screen state structure, or delete flow logic
- Do not redesign the edit UI
- Do not broaden into unrelated wording cleanup outside the recurring-entry edit flow

Done criteria
- Recurring-entry edit loading, missing-entry, and delete-confirmation copy no longer refers to a saved entry
- Any affected edit-flow tests or expectations are aligned with the new wording
- Edit-flow behavior remains unchanged
- The smallest affected-scope verification is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The follow-up is still localized, but it spans user-visible copy, state surfaces, and verification for the edit flow.
