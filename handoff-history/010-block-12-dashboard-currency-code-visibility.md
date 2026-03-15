Next block name
BLOCK 12 - Dashboard Currency Code Visibility

Objective
Expose each recurring entry's saved currency code in the dashboard summary lists so multi-currency entries are visibly distinguishable, while keeping the existing monthly-total math and amount-formatting behavior unchanged.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/main/res/values/strings.xml

Constraints
- Surface the saved `currencyCode` from recurring entries without introducing conversion, exchange-rate math, or currency-normalized totals
- Preserve the existing monthly recurring total calculation exactly as it works today
- Keep the dashboard UI change small and readable; a lightweight label or supporting line is enough
- Choose the smallest meaningful verification for the dashboard currency-visibility behavior

What not to change
- Do not revisit recurring create/edit form behavior or Room schema for this block
- Do not trigger currency metadata sync from dashboard rows or cards
- Do not introduce base-currency preferences, converted summaries, or per-entry converted amounts
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Dashboard state carries through each recurring entry's saved currency code where needed for rendering
- Saved recurring entry cards and upcoming-payment rows visibly show the stored currency code
- Existing dashboard totals and amount formatting remain unchanged
- Focused dashboard tests cover the new currency-code visibility behavior
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next step is mostly presentation/state plumbing with focused dashboard verification and limited product-scope risk.
