Next block name
BLOCK 46 - Dashboard Scenario State Builder Consolidation

Objective
Reduce the repeated `val items = upcomingPaymentScenarioItems(...); return dashboardState(...)` pattern that still exists across the scenario-specific state builders in `DashboardScreenTest.kt` by introducing one narrow file-local state-builder helper, while preserving all current dashboard assertions and fixture readability.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the work dashboard-scoped and test-only
- Reuse file-local helpers only; do not introduce shared test infrastructure outside this file
- Preserve all existing dashboard strings, semantics, action assertions, and scenario fixture call sites
- Keep scenario-specific helper names readable and behaviorally identical
- Update `handoff/validation-report.md` with the final validation result for the next block

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, persistence, navigation, or currency sync behavior
- Do not modify Room schema, repositories, DAOs, AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated scenario-state assembly in `DashboardScreenTest.kt` is routed through one file-local helper where it improves duplication without obscuring intent
- `DashboardScreenTest.kt` remains behaviorally equivalent and readable
- Verification again targets the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted and recorded, including any environment blockers if they persist

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is another local dashboard test cleanup with limited surface area, but it still needs careful preservation of fixture readability and state defaults.
