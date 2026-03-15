Next block name
BLOCK 15 - Dashboard Mixed-Currency Summary Neutral Amount Formatting

Objective
Reduce ambiguity in the dashboard summary amount when multiple active currencies are present by replacing the locale-currency presentation with a neutral aggregate amount style only for mixed-currency dashboards, while keeping the existing monthly total math unchanged and preserving the simpler single-currency experience.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/res/values/strings.xml
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Preserve the existing `monthlyRecurringTotal` calculation exactly as it works today
- Do not introduce exchange-rate math, converted totals, or a base-currency setting
- Only adjust summary amount presentation when active entries span multiple currencies
- Keep the current summary amount formatting for single-currency dashboards unless a very small shared refactor is needed
- Choose the smallest meaningful verification for the formatting behavior you change

What not to change
- Do not revisit recurring create/edit form flows or currency selection UX in this block
- Do not change Room schema, DAO contracts, or currency metadata sync behavior
- Do not redesign dashboard sections beyond summary amount formatting needed for mixed-currency clarity
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Mixed-currency dashboards no longer present the aggregate monthly total with a misleading locale currency symbol
- Single-currency dashboards keep their current summary amount experience
- Monthly total math remains unchanged
- Focused dashboard tests cover the mixed-currency summary amount formatting behavior
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step stays tightly scoped, but it needs careful UI wording/formatting choices so mixed-currency totals become clearer without expanding into real currency conversion.
