Next block name
BLOCK 25 - Dashboard Summary Card Accessibility Summary

Objective
Expose the dashboard summary card as a merged accessibility summary that clearly announces the monthly total, active-entry count, mixed-currency note when present, and currency metadata status text without changing the visible summary content or retry behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- app/src/main/res/values/strings.xml

Constraints
- Keep the work dashboard-scoped and presentation-only
- Preserve the existing visible summary card text and retry action behavior
- Do not change the mixed-currency message logic or currency sync state logic
- Prefer merged semantics over duplicative child announcements when exposing the summary card
- Choose the smallest meaningful verification for the affected summary-card accessibility behavior

What not to change
- Do not change recurring-entry create/edit flows, validation, or persistence behavior
- Do not modify Room schema, repositories, DAOs, or currency sync behavior outside summary-card presentation
- Do not alter saved recurring-entry card behavior, upcoming-payment sorting, semantics, or interaction behavior
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The dashboard summary card exposes a coherent merged accessibility summary for its currently displayed content
- The summary card still shows the same visible monthly total, active-entry count, mixed-currency note, and currency metadata status text as before
- The retry button remains available with its current behavior when the existing state conditions require it
- Dashboard presentation tests cover the summary-card accessibility summary at the smallest practical scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next change stays localized to summary-card semantics but needs careful handling of conditional text and the retry action.
