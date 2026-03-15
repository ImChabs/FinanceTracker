Next block name
BLOCK 07 - Dashboard Upcoming Payments Section

Objective
Add a focused upcoming-payments section to the dashboard so the user can quickly see the next recurring charges in date order alongside the existing summary and saved-entry list.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardStateTest.kt
- app/src/main/res/values/strings.xml

Constraints
- Keep this block focused on deriving and presenting upcoming payments from the existing local recurring-entry data
- Reuse the current dashboard data flow instead of introducing a second dashboard repository path
- Prefer a small, readable dashboard addition that works with the current layout on mobile
- Decide and document a simple rule for which entries count as upcoming, such as active entries sorted by nearest next payment date
- Prefer the smallest meaningful verification for the affected `app` scope

What not to change
- Do not redesign the whole dashboard or replace the existing saved recurring entries section
- Do not add filtering, search, reminders, or notification behavior yet
- Do not add remote/API work
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Dashboard state exposes the data needed for an upcoming-payments section
- The dashboard UI shows upcoming payments in a clear order with reasonable empty handling
- Existing summary and saved-entry behavior remain intact
- Focused verification is added or updated for the dashboard derivation logic
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The work stays within the dashboard feature, but it still needs careful state derivation and a UI addition that fits the current screen cleanly.
