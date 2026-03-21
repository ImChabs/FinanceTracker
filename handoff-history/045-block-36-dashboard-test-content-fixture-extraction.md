Next block name
BLOCK 36 - Dashboard Test Content Fixture Extraction

Objective
Reduce repeated `composeRule.setContent { FinanceTrackerTheme { DashboardScreen(...) } }` boilerplate in `DashboardScreenTest.kt` by extracting a small local test helper that keeps each scenario readable without changing dashboard behavior, assertions, or test coverage.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and test-only
- Preserve all current dashboard behavior, visible copy, accessibility summaries, click behavior, and assertion coverage
- Prefer a small helper local to the existing dashboard test file over broader test infrastructure changes
- Do not broaden into production refactors or unrelated Compose test cleanup
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, currency sync behavior, or navigation flows
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated dashboard test content setup is extracted into a clear local helper in `DashboardScreenTest.kt`
- Existing dashboard tests remain behaviorally equivalent and readable
- Verification covers the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a narrow follow-up test-maintenance cleanup in one file with low behavioral risk.
