Next block name
BLOCK 49 - Dashboard Remaining State Surface Refresh

Objective
Complete the remaining visual-refresh work on the dashboard by bringing the loading, empty, and any still-older lightweight state treatments into the same surface, spacing, and hierarchy system now used by the shared form and refreshed recurring edit states.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/core/designsystem/theme/ComponentDefaults.kt
- app/src/main/res/values/strings.xml
- handoff/validation-report.md

Constraints
- Keep dashboard behavior, accessibility semantics, navigation triggers, strings meaning, and data/state flow unchanged
- Prefer extending shared design-system defaults when multiple dashboard state surfaces need the same treatment
- Keep the refresh moderate and aligned with the existing evergreen and warm-neutral Material 3 direction established in the current codebase

What not to change
- Do not modify DashboardViewModel, repository/domain/data code, or navigation wiring
- Do not broaden into recurring-form work that was already completed in this block
- Do not touch unrelated workspace changes under `.idea/` or `.vscode/`

Done criteria
- Dashboard loading and empty treatments use refreshed card/surface presentation instead of older plain text or mismatched hierarchy
- Any touched lightweight dashboard state messaging feels visually consistent with the summary and upcoming-payment surface system
- Shared styling extracted during the work lands in the design-system layer when it is genuinely reused
- The smallest meaningful compile validation is attempted again and recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The remaining work is still presentation-only, but it needs careful consistency updates around existing dashboard semantics and accessibility treatments.
