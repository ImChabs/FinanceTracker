Next block name
BLOCK 26 - Dashboard Empty State Card Accessibility Summary

Objective
Expose the dashboard empty-state card as a merged accessibility summary that announces the existing empty title and body copy together without changing the visible text, loading behavior, or dashboard empty-state logic.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the existing empty-state title and body text exactly as shown today
- Do not change dashboard loading, saved-entry, summary-card, or retry behavior
- Prefer merged semantics over duplicate child announcements for the empty-state card
- Choose the smallest meaningful verification for the affected empty-state accessibility behavior

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter saved recurring-entry card behavior, upcoming-payment behavior, or summary-card semantics added in the previous block
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The dashboard empty-state card exposes a coherent merged accessibility summary for its existing title and body
- The empty-state card still shows the same visible text as before
- Dashboard presentation tests cover the empty-state accessibility summary at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: The next change is a small, localized semantics refinement with straightforward test coverage.
