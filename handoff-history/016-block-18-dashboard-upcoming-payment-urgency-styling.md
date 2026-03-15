Next block name
BLOCK 18 - Dashboard Upcoming Payment Urgency Styling

Objective
Improve the dashboard upcoming-payments section by adding lightweight visual emphasis for urgent relative due states, especially overdue and due-today items, so the new relative context is easier to scan without changing the underlying ordering or data model.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/res/values/strings.xml
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Preserve the existing ISO-backed persistence model and upcoming-payment ordering
- Reuse the current relative due-context model instead of replacing it with a new date-status abstraction
- Keep the styling treatment within dashboard presentation unless a tiny adjacent extraction is required for testability
- Do not hide invalid or legacy date strings; they should continue to fall back gracefully
- Prefer subtle Material 3-consistent emphasis over a dashboard redesign
- Choose the smallest meaningful verification for the affected dashboard behavior

What not to change
- Do not modify recurring create/edit form inputs, validation rules, or submission payloads
- Do not change Room schema, DAO contracts, repository interfaces, or remote currency metadata flows
- Do not alter upcoming-payment sorting, filtering, or inclusion rules
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Overdue and due-today upcoming payments are more visually prominent than non-urgent ones
- The relative due-context text introduced in Block 17 remains intact and readable
- Invalid or legacy date strings still appear with existing fallback behavior
- Dashboard presentation tests cover the urgency styling behavior at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next block stays presentation-scoped, but it needs measured UI treatment and focused verification without drifting into a larger dashboard redesign.
