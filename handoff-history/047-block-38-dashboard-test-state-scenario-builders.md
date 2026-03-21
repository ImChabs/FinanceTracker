Next block name
BLOCK 38 - Dashboard Test State Scenario Builders

Objective
Reduce repeated `DashboardState(...)` setup in `DashboardScreenTest.kt` by extracting a few small local state builders for the common dashboard scenarios used across the tests, while preserving all current dashboard behavior, assertions, and coverage.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and test-only
- Prefer a few local `DashboardState` builders in the existing test file over shared test infrastructure
- Preserve current visible copy, accessibility summaries, click behavior, and assertion coverage
- Do not broaden into production refactors, screenshot tests, or unrelated Compose test cleanup
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, currency sync behavior, or navigation flows
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated `DashboardState(...)` scenario setup is reduced through clear local builders in `DashboardScreenTest.kt`
- Existing dashboard tests remain behaviorally equivalent and readable
- Verification covers the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a narrow test-maintenance follow-up in one file with low behavioral risk.
