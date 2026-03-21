Next block name
BLOCK 35 - Dashboard Locale Test Fixture Extraction

Objective
Reduce repeated locale setup and teardown boilerplate in `DashboardScreenTest.kt` by extracting a small local helper for tests that require a stable `Locale.US`, without changing dashboard behavior, test scenarios, or assertion coverage.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and test-only
- Preserve all current dashboard behavior, visible copy, accessibility summaries, and assertion coverage
- Prefer a small helper local to the existing dashboard test file over broader test reorganization
- Do not broaden into production refactors or unrelated Compose test cleanup
- Choose the smallest meaningful verification for the affected dashboard instrumentation test compile scope
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, currency sync behavior, or navigation flows
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated `Locale.setDefault(Locale.US)` setup and restoration is extracted into a clear local helper in `DashboardScreenTest.kt`
- Existing dashboard tests that rely on US locale remain behaviorally equivalent and readable
- Verification covers the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is another narrow dashboard test-maintenance cleanup with localized edits and low behavioral risk.
