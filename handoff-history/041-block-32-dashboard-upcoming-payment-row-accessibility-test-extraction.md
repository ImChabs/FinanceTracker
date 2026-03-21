Next block name
BLOCK 32 - Dashboard Upcoming Payment Row Accessibility Test Extraction

Objective
Reduce duplication in dashboard upcoming-payment accessibility instrumentation coverage by extracting focused test helpers for locating upcoming-payment rows and asserting their merged semantics, without changing dashboard behavior or accessibility copy.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Keep the work dashboard-scoped and test-only
- Preserve all current dashboard behavior, visible copy, accessibility summaries, action labels, urgency semantics, and click assertions
- Prefer small helper extraction inside the existing dashboard test file over broader test reorganization
- Do not broaden into production refactors or unrelated Compose test cleanup
- Choose the smallest meaningful verification for the affected dashboard instrumentation test scope
- Use the repo-local validation-fix loop and update `handoff/validation-report.md` with the final validation result

What not to change
- Do not modify dashboard production code unless a tiny adjacent testability fix is strictly required
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, currency sync behavior, or navigation flows
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Repeated upcoming-payment semantics lookup/assertion logic is extracted into clear local test helpers
- Existing upcoming-payment accessibility tests remain behaviorally equivalent and readable
- Verification covers at least one affected dashboard instrumentation test at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a contained test-maintenance block with minimal behavioral risk and focused verification.
