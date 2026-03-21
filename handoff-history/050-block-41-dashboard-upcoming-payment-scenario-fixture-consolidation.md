Next block name
BLOCK 41 - Dashboard Upcoming Payment Scenario Fixture Consolidation

Objective
Reduce repeated `savedEntry(...)` and `upcomingPayment(...)` scenario setup across the dashboard upcoming-payment test builders in `DashboardScreenTest.kt` by introducing one or two narrow file-local scenario fixture helpers, while preserving all existing assertions, semantics copy, click labels, and interaction checks.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the work dashboard-scoped and test-only
- Continue using file-local helpers in the existing test file rather than shared fixtures or cross-file infrastructure
- Preserve every current test scenario, accessibility summary, click label, visible copy, and action assertion
- Do not broaden into production refactors, test framework redesign, or unrelated formatting cleanup
- Update `handoff/validation-report.md` with the final validation result for the next block

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, persistence, navigation, or currency sync behavior
- Do not modify Room schema, repositories, DAOs, AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated upcoming-payment scenario entry lists in local test builders are reduced through clear local fixture helpers
- Existing dashboard tests remain behaviorally equivalent and readable
- Verification again targets the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted and recorded, including any environment blockers if they persist

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a small follow-on cleanup in the same test file with low product risk and limited coordination.
