Next block name
BLOCK 34 - Dashboard Static Accessibility Summary Test Extraction

Objective
Reduce duplication in dashboard accessibility instrumentation coverage by extracting focused local test helpers for merged content-description assertions on static dashboard cards such as the summary card and empty states, without changing dashboard behavior or accessibility copy.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and test-only
- Preserve all current dashboard behavior, visible copy, accessibility summaries, and assertion coverage
- Prefer small helper extraction inside the existing dashboard test file over broader test reorganization
- Do not broaden into production refactors or unrelated Compose test cleanup
- Choose the smallest meaningful verification for the affected dashboard instrumentation test compile scope
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, currency sync behavior, or navigation flows
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated merged content-description assertion logic for static dashboard cards is extracted into clear local test helpers
- Existing summary and empty-state accessibility tests remain behaviorally equivalent and readable
- Verification covers the smallest practical dashboard instrumentation test compile scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is another narrow dashboard test-maintenance block with minimal behavioral risk and localized verification.
