Next block name
BLOCK 45 - Dashboard Scenario Fixture Mapping Consolidation

Objective
Reduce the duplicated saved-entry-to-`DashboardRecurringEntryItem` mapping that still exists in `savedEntryScenarioItems` and `upcomingPaymentScenarioItems` inside `DashboardScreenTest.kt` by introducing one narrow file-local mapper for `SavedEntryScenarioFields`, while preserving all current dashboard assertions and fixture call sites.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the work dashboard-scoped and test-only
- Reuse file-local helpers only; do not introduce shared test infrastructure outside this file
- Preserve all existing dashboard strings, semantics, action assertions, and state-builder behavior
- Keep `SavedEntryScenarioFixture(...)` and `UpcomingPaymentScenarioFixture(...)` call sites readable and behaviorally identical
- Update `handoff/validation-report.md` with the final validation result for the next block

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, persistence, navigation, or currency sync behavior
- Do not modify Room schema, repositories, DAOs, AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The repeated saved-entry mapping logic is routed through one file-local helper based on `SavedEntryScenarioFields`
- `DashboardScreenTest.kt` remains behaviorally equivalent and readable
- Verification again targets the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted and recorded, including any environment blockers if they persist

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a local helper cleanup in one dashboard test file, with low product risk but a need to preserve existing fixture behavior exactly.
