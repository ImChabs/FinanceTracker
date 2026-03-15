Next block name
BLOCK 20 - Dashboard Upcoming Payment Row Accessibility Summary

Objective
Improve screen-reader usability for dashboard upcoming payments by giving each row a concise, well-ordered accessibility summary that communicates the key payment details clearly without forcing users to piece the row together from separate text nodes.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/res/values/strings.xml
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt

Constraints
- Preserve the existing upcoming-payment ordering, inclusion rules, relative due-context model, and urgency styling
- Keep the work presentation-scoped; do not introduce new domain or data abstractions
- Reuse the current displayed payment details rather than inventing new dashboard data
- Keep invalid or legacy date strings on their existing graceful fallback path
- Avoid duplicative or excessively verbose accessibility output, especially once urgency state is already exposed
- Choose the smallest meaningful verification for the affected accessibility behavior

What not to change
- Do not modify recurring create/edit form behavior, validation, or persistence payloads
- Do not change Room schema, DAO contracts, repositories, or currency sync flows
- Do not alter upcoming-payment sorting, filtering, or data derivation rules
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Upcoming payment rows expose a concise accessibility summary covering the key payment details in a sensible reading order
- Existing visible text, relative due-context text, and urgency semantics remain intact
- Non-urgent rows stay readable without noisy duplication
- Dashboard presentation tests cover the new row-level accessibility behavior at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The block remains presentation-scoped, but it needs careful semantics design so screen readers get a better summary without duplicating visible content or regressing the urgency improvements.
