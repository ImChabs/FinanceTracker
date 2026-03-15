Next block name
BLOCK 21 - Dashboard Upcoming Payment Row Navigation

Objective
Make dashboard upcoming-payment rows tappable so users can jump directly from an upcoming payment to the existing recurring-entry edit flow, while preserving the new accessibility summary semantics and current upcoming-payment ordering and inclusion rules.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardAction.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardViewModel.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Reuse the existing recurring-entry navigation path instead of adding a second edit destination
- Keep the work dashboard-scoped; do not introduce new domain or data abstractions
- Preserve upcoming-payment sorting, filtering, relative due-context behavior, urgency styling, and row accessibility summaries
- Ensure click semantics remain compatible with the row-level accessibility summary and urgency state description
- Choose the smallest meaningful verification for the affected interaction behavior

What not to change
- Do not modify recurring create/edit form fields, validation, or persistence behavior
- Do not change Room schema, DAO contracts, repositories, or currency sync flows
- Do not alter the saved recurring-entry card interaction model unless required to keep behavior consistent
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Tapping an upcoming-payment row dispatches the correct dashboard action for the matching recurring entry
- Existing navigation from the dashboard still routes to the recurring-entry edit screen
- Upcoming-payment row accessibility summary and urgency semantics remain intact after adding click behavior
- Dashboard presentation tests cover the new row interaction at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The block is still presentation-led, but it needs careful coordination between Compose click semantics, existing dashboard actions, and regression-safe accessibility behavior.
