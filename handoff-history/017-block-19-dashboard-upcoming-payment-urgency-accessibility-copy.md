Next block name
BLOCK 19 - Dashboard Upcoming Payment Urgency Accessibility Copy

Objective
Improve the accessibility of the dashboard upcoming-payments urgency treatment by giving overdue and due-today rows explicit, user-friendly semantic descriptions so assistive technologies convey the same urgency signal as the new visual styling.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/res/values/strings.xml
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Preserve the existing upcoming-payment ordering, inclusion rules, and relative due-context model
- Keep the work presentation-scoped; do not introduce new domain or data abstractions
- Reuse the current urgency mapping unless a tiny adjacent adjustment is required for accessibility clarity
- Keep invalid or legacy date strings on their existing graceful fallback path
- Prefer concise, Material-consistent accessibility copy over adding more visible dashboard chrome
- Choose the smallest meaningful verification for the affected accessibility behavior

What not to change
- Do not modify recurring create/edit form behavior, validation, or persistence payloads
- Do not change Room schema, DAO contracts, repositories, or currency sync flows
- Do not alter upcoming-payment sorting, filtering, or data derivation rules
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Overdue and due-today upcoming payment rows expose explicit accessibility descriptions for their urgency state
- Non-urgent upcoming payments remain readable without redundant or noisy accessibility output
- Existing relative due-context text and visual urgency styling remain intact
- Dashboard presentation tests cover the accessibility semantics at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The block is still small and presentation-scoped, but it needs careful semantics wording and focused UI verification without overcomplicating the dashboard.
