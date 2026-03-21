Next block name
BLOCK 48 - Visual Refresh Rollout for Shared Form Controls and Remaining UI States

Objective
Continue the visual refresh by applying the new design foundation to the remaining high-value shared surfaces: refine recurring-form controls and action hierarchy, align the remaining loading/missing/empty/error treatments with the new surface system, and keep product behavior unchanged.

Relevant files
- app/src/main/java/com/example/newfinancetracker/core/designsystem/theme/Color.kt
- app/src/main/java/com/example/newfinancetracker/core/designsystem/theme/Theme.kt
- app/src/main/java/com/example/newfinancetracker/core/designsystem/theme/Type.kt
- app/src/main/java/com/example/newfinancetracker/core/designsystem/theme/Shape.kt
- app/src/main/java/com/example/newfinancetracker/core/designsystem/theme/Spacing.kt
- app/src/main/java/com/example/newfinancetracker/core/designsystem/theme/ComponentDefaults.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/form/RecurringEntryForm.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/RecurringEntryEditScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- handoff/validation-report.md

Constraints
- Treat the new evergreen plus warm-neutral palette, explicit typography, rounded shape scale, spacing tokens, and shared top app bar/card defaults as the established direction
- Keep all navigation, validation rules, state flow, data flow, strings, and business logic unchanged unless a tiny UI-only adjustment is strictly required
- Prefer extending shared theme or shared-form styling over adding screen-specific styling hacks
- Keep the refresh moderate, coherent, and aligned with Material 3 patterns

What not to change
- Do not modify ViewModel behavior, repository/domain/data code, navigation destinations, validation logic, or API behavior
- Do not turn the app into a different product identity or introduce flashy decorative UI
- Do not touch unrelated workspace changes under `.idea/` or `.vscode/`

Done criteria
- Remaining recurring-form micro-surfaces such as chip groups, currency selection presentation, destructive action hierarchy, and any touched dialog styling feel consistent with the new foundation
- Remaining dashboard state surfaces or lightweight state treatments that still look visually older are brought into the same card, spacing, and hierarchy system
- Any additional styling shared across screens lands in the design-system layer or shared form shell rather than as one-off per-screen overrides
- The smallest meaningful compile validation is attempted again and recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next block is still presentation-only, but it needs careful consistency work across several shared Compose surfaces without drifting into a broader redesign.
