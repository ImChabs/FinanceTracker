Next block name
BLOCK 31 - Dashboard Upcoming Payment Row Accessibility Action Label

Objective
Expose a clearer accessibility action label for tappable upcoming-payment rows so assistive technologies announce the row action more intentionally without changing visible copy, urgency semantics, or click behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve current upcoming-payment visible text, urgency state descriptions, accessibility summaries, and row layout
- Do not change loading logic, summary card behavior, saved-entry card behavior, or retry/add-entry behavior
- Prefer a localized accessibility action-label refinement over broader row restructuring
- Choose the smallest meaningful verification for the affected upcoming-payment accessibility behavior
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter dashboard summary totals, loading-state copy, empty-state copy, or saved recurring-entry card semantics
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Upcoming-payment rows expose an explicit accessibility action label without changing their visible text
- Existing urgency semantics and recurring-entry click dispatch remain unchanged
- Dashboard presentation tests cover the upcoming-payment action-label behavior at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This should be another small, localized dashboard semantics update with focused Compose test coverage.
