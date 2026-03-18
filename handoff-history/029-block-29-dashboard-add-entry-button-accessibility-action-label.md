Next block name
BLOCK 29 - Dashboard Add Entry Button Accessibility Action Label

Objective
Expose the dashboard add-recurring-entry button with a clearer accessibility action label while preserving its visible button text and existing add-entry dispatch behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the visible add button text exactly as shown today
- Do not change add-entry navigation dispatch behavior, loading logic, or dashboard layout
- Prefer a localized semantics/accessibility-label refinement over broader UI restructuring
- Choose the smallest meaningful verification for the affected add-button accessibility behavior
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter dashboard summary totals, upcoming-payment rows, saved recurring-entry cards, empty-state copy, or retry-button behavior
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The dashboard add-recurring-entry button exposes a clearer accessibility action label without changing its visible text
- The add action still dispatches exactly as before
- Dashboard presentation tests cover the add button accessibility behavior at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is another small, localized dashboard semantics refinement with straightforward Compose test coverage.
