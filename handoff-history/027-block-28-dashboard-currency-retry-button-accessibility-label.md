Next block name
BLOCK 28 - Dashboard Currency Retry Button Accessibility Label

Objective
Expose the dashboard currency-metadata retry button with a clearer accessibility action label that preserves the visible button text while giving assistive tech better context about the retry action.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the visible retry button text exactly as shown today
- Do not change currency sync state logic, snackbar behavior, or retry dispatch behavior
- Prefer a localized semantics/accessibility-label refinement over broader UI restructuring
- Choose the smallest meaningful verification for the affected retry-button accessibility behavior

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter dashboard summary totals, upcoming-payment rows, saved recurring-entry cards, or empty-state copy
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The dashboard currency retry button exposes a clearer accessibility label without changing its visible text
- The retry action still dispatches exactly as before
- Dashboard presentation tests cover the retry button accessibility behavior at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a small, localized dashboard semantics refinement with straightforward Compose test coverage.
