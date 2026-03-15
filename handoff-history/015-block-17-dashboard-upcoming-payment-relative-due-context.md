Next block name
BLOCK 17 - Dashboard Upcoming Payment Relative Due Context

Objective
Improve dashboard scanability by adding relative due context for valid upcoming-payment dates, such as overdue, due today, tomorrow, or in X days, while preserving the existing ISO-backed storage, ordering, and fallback behavior introduced for date display.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/res/values/strings.xml
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Preserve the existing ISO-backed persistence model and do not change stored date values
- Keep current upcoming-payment sorting behavior unchanged
- Reuse the existing graceful fallback behavior for invalid or legacy date strings instead of hiding them or crashing
- Keep the implementation local to dashboard presentation/state unless a tiny adjacent extraction is required for testability
- Make the relative-date logic deterministic and testable without depending on wall-clock flakiness
- Choose the smallest meaningful verification for the dashboard behavior you change

What not to change
- Do not modify recurring create/edit form inputs, validation rules, or submission payloads
- Do not change Room schema, DAO contracts, repository interfaces, or remote currency metadata flows
- Do not redesign dashboard layout beyond the relative due-context treatment needed for readability
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Upcoming payments show relative due context for valid dates in a way that is understandable at a glance
- Relative due context remains consistent with the already formatted human-readable date display
- Invalid or legacy date strings still remain visible as-is where shown on the dashboard
- Existing upcoming-payment ordering remains unchanged
- Focused dashboard tests cover the new relative-date behavior with deterministic expectations
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The block is still presentation-scoped, but date-relative logic introduces edge cases around clock handling and test determinism.
