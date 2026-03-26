Next block name
BLOCK 85 - Dashboard Active Entry Accessibility Copy Alignment

Objective
Align the dashboard recurring-entry card accessibility and action-label copy with the new active-only section wording so the surfaced cards are no longer described as generic saved entries.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/res/values/strings.xml
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the active-only filtering behavior from Block 84 unchanged
- Preserve card click behavior and existing merged accessibility structure unless wording adjustments require a minimal expectation update
- Limit the block to accessibility/copy alignment for the recurring-entry cards; do not broaden into summary or upcoming-payment wording
- Use the smallest meaningful verification for the touched presentation scope and record the result in `handoff/validation-report.md`

What not to change
- Do not change recurring-entry repository contracts, dashboard state mapping, or section empty-state behavior
- Do not redesign the dashboard UI
- Do not broaden the block into unrelated accessibility cleanup outside the dashboard recurring-entry cards

Done criteria
- Dashboard recurring-entry card action labels no longer refer to the cards as saved entries
- Any user-visible or accessibility-only card copy that still conflicts with the active-only section wording is aligned
- Existing dashboard card click behavior remains intact
- The smallest affected-scope verification is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The follow-up is still focused, but it needs careful alignment between UI strings, accessibility summaries, and existing Compose UI expectations.
