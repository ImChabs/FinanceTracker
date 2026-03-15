Next block name
BLOCK 14 - Dashboard Multi-Currency Summary Clarity

Objective
Clarify the dashboard summary card when active recurring entries span multiple saved currencies so users can understand that the existing monthly total is still an unconverted aggregate, while keeping the current total math and per-entry formatting behavior unchanged.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Preserve the existing `monthlyRecurringTotal` calculation exactly as it works today
- Do not introduce exchange-rate math, converted totals, or a base-currency setting
- Only add summary copy/state needed to explain mixed-currency totals when multiple active currency codes are present
- Keep single-currency dashboards visually simple unless a small supporting refactor is required
- Choose the smallest meaningful verification for dashboard summary behavior

What not to change
- Do not revisit recurring create/edit form flows or currency selection UX in this block
- Do not change Room schema, DAO contracts, or currency metadata sync behavior
- Do not redesign dashboard sections beyond summary messaging needed for multi-currency clarity
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Dashboard state can identify when active entries span multiple currency codes
- Summary card surfaces concise mixed-currency guidance only when that condition is true
- Single-currency dashboards keep the current summary experience without unnecessary extra copy
- Monthly total math remains unchanged
- Focused dashboard tests cover the mixed-currency summary behavior
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step is mostly UI/state clarification work, but it still needs careful scope control so the summary becomes clearer without implying currency conversion that does not exist.
