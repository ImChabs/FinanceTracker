Next block name
BLOCK 24 - Dashboard Saved Recurring Entry Card Accessibility State Description

Objective
Expose the active or inactive status of dashboard saved recurring-entry cards through a dedicated accessibility state description so screen-reader users hear the current saved-entry state distinctly, while preserving the existing merged summary, edit action label, and edit navigation behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- app/src/main/res/values/strings.xml

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the current saved recurring-entry card merged summary content
- Preserve the saved recurring-entry card accessibility click label and edit navigation behavior
- Keep the active/inactive copy aligned with the existing dashboard status wording unless a small accessibility-specific variation is required
- Choose the smallest meaningful verification for the affected saved-card accessibility behavior

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter upcoming-payment sorting, semantics, or interaction behavior
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Each saved recurring-entry card exposes an accessibility state description for its active or inactive status
- The saved recurring-entry card keeps its existing merged accessibility summary
- The saved recurring-entry card keeps its contextual accessibility click label and still dispatches the existing recurring-entry edit action
- Dashboard presentation tests cover the saved-card accessibility state description at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: The next change should stay localized to saved-card semantics and a focused dashboard accessibility test.
