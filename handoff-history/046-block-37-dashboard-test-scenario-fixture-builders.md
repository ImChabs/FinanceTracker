Next block name
BLOCK 37 - Dashboard Test Scenario Fixture Builders

Objective
Reduce repeated `DashboardRecurringEntryItem(...)` and `DashboardUpcomingPaymentItem(...)` construction in `DashboardScreenTest.kt` by extracting a few small local fixture builders that keep scenario setup concise without changing dashboard behavior, assertions, or test coverage.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and test-only
- Preserve all current dashboard behavior, visible copy, accessibility summaries, click behavior, and assertion coverage
- Prefer small helper builders local to the existing dashboard test file over broader shared test infrastructure
- Do not broaden into production refactors, screenshot tests, or unrelated Compose test cleanup
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, currency sync behavior, or navigation flows
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated recurring-entry and upcoming-payment test fixture construction is reduced through clear local builders in `DashboardScreenTest.kt`
- Existing dashboard tests remain behaviorally equivalent and readable
- Verification covers the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is another narrow test-maintenance cleanup in one file with low behavioral risk.
