Next block name
BLOCK 27 - Dashboard Upcoming Payments Empty State Accessibility Summary

Objective
Expose the dashboard upcoming-payments empty-state card as a merged accessibility summary that announces its existing empty message once without changing the visible text, section structure, or upcoming-payments logic.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the existing upcoming-payments empty-state text exactly as shown today
- Do not change dashboard loading, main empty state, saved-entry cards, summary card, or retry behavior
- Prefer merged semantics over duplicate child announcements for the upcoming-payments empty-state card
- Choose the smallest meaningful verification for the affected empty-state accessibility behavior

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior
- Do not alter saved recurring-entry card behavior, upcoming-payment row behavior, or empty-state copy outside the upcoming-payments empty card
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The dashboard upcoming-payments empty-state card exposes a coherent merged accessibility summary for its existing message
- The upcoming-payments empty-state still shows the same visible text as before
- Dashboard presentation tests cover the upcoming-payments empty-state accessibility summary at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: The next change is another small, localized semantics refinement with straightforward Compose test coverage.
