Next block name
BLOCK 22 - Dashboard Saved Recurring Entry Card Accessibility Summary

Objective
Add a merged accessibility summary for dashboard saved recurring-entry cards so each tappable card exposes its key financial details and active status as a single accessible surface while preserving the existing edit navigation behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the current saved recurring-entry card click behavior and edit navigation path
- Keep existing amount formatting, date formatting, active/inactive copy, notes visibility, and card ordering unchanged
- Ensure merged semantics do not remove useful click semantics from the card surface
- Choose the smallest meaningful verification for the affected accessibility behavior

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter upcoming-payment sorting, semantics, or interaction behavior
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Each saved recurring-entry card exposes a single accessibility summary that includes the key entry details users need when browsing the dashboard
- The saved recurring-entry card remains tappable and still dispatches the existing recurring-entry edit action
- Dashboard presentation tests cover the new saved-card accessibility summary at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The change is still localized to dashboard presentation, but it needs careful Compose semantics work to improve accessibility without regressing existing click behavior.
