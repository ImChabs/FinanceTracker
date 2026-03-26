Next block name
BLOCK 84 - Dashboard Active Recurring Entries Section

Objective
Align the dashboard with the MVP goal of showing active recurring entries instead of a generic saved-entry list by filtering the section to active items only, updating the section copy/counts, and preserving edit navigation plus empty-state behavior for users who only have inactive entries.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardViewModel.kt
- app/src/main/res/values/strings.xml
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- handoff/validation-report.md

Constraints
- Keep the dashboard summary card and upcoming-payments section behavior unchanged unless a small adjacent state-shape adjustment is required to support the active-entry list cleanly
- Preserve dashboard item click behavior so tapping an active recurring entry still routes to edit for that entry
- If inactive entries still need to contribute to any other state, limit that follow-up to the minimum required mapping changes
- Use the smallest meaningful verification for the touched presentation scope, likely the targeted compile script unless the block adds or changes dashboard tests enough to warrant an additional androidTest compile run

What not to change
- Do not redesign the dashboard visual style
- Do not change recurring-entry repository contracts or database schema unless a small in-scope bug forces it
- Do not broaden the dashboard into sorting/filtering work beyond switching this section to active entries

Done criteria
- The dashboard entries section renders only active recurring entries
- The section title and count copy no longer describe the list as generic saved entries
- A dashboard state with only inactive entries shows the intended section-empty behavior without regressing the overall screen empty state for truly no-entry setups
- Existing edit-navigation behavior for listed entries still works
- The smallest affected-scope verification is recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The block is a focused dashboard presentation change with some state-mapping and test-scenario coordination, but it should stay inside one feature slice.
