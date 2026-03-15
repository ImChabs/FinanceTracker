Next block name
BLOCK 13 - Currency-Aware Entry Amount Formatting

Objective
Format each saved recurring entry amount and upcoming payment amount using that entry's saved currency code so the visible symbol aligns with the saved currency, while keeping the existing monthly total math and summary behavior unchanged.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Preserve the existing `monthlyRecurringTotal` calculation exactly as it works today
- Do not introduce exchange-rate math, converted totals, or base-currency preferences
- Apply currency-aware formatting only to per-entry and upcoming-payment amounts unless a small supporting refactor is required
- Handle invalid or unsupported currency codes safely with a stable fallback instead of crashing
- Choose the smallest meaningful verification for formatter behavior and dashboard rendering

What not to change
- Do not revisit recurring create/edit form state or repository wiring for this block
- Do not change Room schema, DAO contracts, or remote currency sync flows
- Do not redesign the dashboard layout beyond what is required for amount rendering
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Saved recurring entry cards format their amounts using the entry's saved currency code
- Upcoming payment rows format their amounts using the entry's saved currency code
- Dashboard summary total behavior remains unchanged
- Invalid currency codes do not crash rendering and fall back predictably
- Focused dashboard tests cover currency-aware amount rendering
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: high
- Rationale: Currency-aware formatting has locale and fallback edge cases, and the next block should keep multi-currency rendering clearer without accidentally changing summary semantics.
