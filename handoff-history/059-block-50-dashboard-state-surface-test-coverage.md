Next block name
BLOCK 50 - Dashboard State Surface Test Coverage

Objective
Add targeted dashboard Compose UI coverage for the refreshed loading, empty, and upcoming-empty state surfaces so the new card hierarchy and accessibility summaries are locked down without changing dashboard behavior.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- handoff/validation-report.md

Constraints
- Keep the block test-focused unless a tiny production adjustment is required to make the refreshed dashboard states testable
- Preserve existing dashboard behavior, strings meaning, semantics, and action dispatching
- Reuse the existing dashboard screen fixture/state-builder patterns instead of introducing a new test harness

What not to change
- Do not modify DashboardViewModel, repository/domain/data code, or navigation wiring
- Do not broaden into unrelated dashboard visual tweaks beyond tiny testability fixes
- Do not touch unrelated workspace changes under `.idea/` or `.vscode/`

Done criteria
- Compose UI tests cover the loading state surface semantics and visible loading treatment
- Compose UI tests cover the empty dashboard state card content and merged accessibility summary
- Compose UI tests cover the upcoming-payments empty surface content and merged accessibility summary
- The smallest meaningful androidTest compile validation is attempted again and recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next block is bounded, but it needs careful alignment with the existing dashboard test fixtures and Compose semantics assertions.
