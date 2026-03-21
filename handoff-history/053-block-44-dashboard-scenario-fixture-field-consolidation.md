Next block name
BLOCK 44 - Dashboard Scenario Fixture Field Consolidation

Objective
Reduce the duplicated saved-entry field definitions shared by `SavedEntryScenarioFixture` and `UpcomingPaymentScenarioFixture` in `DashboardScreenTest.kt` by introducing one narrow file-local fixture composition path, while preserving every current dashboard assertion and helper call site.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the work dashboard-scoped and test-only
- Reuse file-local fixture/builders only; do not introduce shared test infrastructure outside this file
- Preserve all existing dashboard strings, semantics, action assertions, and state-builder behavior
- Keep constructor defaults readable for the current test scenarios
- Update `handoff/validation-report.md` with the final validation result for the next block

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, persistence, navigation, or currency sync behavior
- Do not modify Room schema, repositories, DAOs, AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated saved-entry fixture fields are consolidated through one file-local path shared by the dashboard scenario fixture builders
- `DashboardScreenTest.kt` remains behaviorally equivalent and readable
- Verification again targets the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted and recorded, including any environment blockers if they persist

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a local test-fixture cleanup in one file, but it touches shared builder structure and needs a careful no-behavior-change pass.
