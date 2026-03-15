Next block name
BLOCK 23 - Dashboard Saved Recurring Entry Card Accessibility Action Label

Objective
Add a contextual accessibility click label for dashboard saved recurring-entry cards so screen-reader users hear that activating the card edits the saved recurring entry, while preserving the existing merged summary and edit navigation behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- app/src/main/res/values/strings.xml

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the current saved recurring-entry card merged summary content and click behavior
- Do not change saved card ordering, formatting, notes visibility, or active/inactive copy
- Keep the action label specific to the saved recurring-entry edit path without changing the destination itself
- Choose the smallest meaningful verification for the affected accessibility behavior

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter upcoming-payment sorting, semantics, or interaction behavior
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Each saved recurring-entry card exposes a contextual accessibility click label that communicates the edit action
- The saved recurring-entry card remains tappable and still dispatches the existing recurring-entry edit action
- Dashboard presentation tests cover the saved-card accessibility click label at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The work is localized, but it needs careful semantics updates and focused test assertions so the new action label improves accessibility without regressing the current merged summary behavior.
