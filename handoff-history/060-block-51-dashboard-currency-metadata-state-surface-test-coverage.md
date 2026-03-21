Next block name
BLOCK 51 - Dashboard Currency Metadata State Surface Test Coverage

Objective
Add targeted dashboard Compose UI coverage for the summary card's currency metadata states so the ready, refreshing, and unavailable/failure surfaces are locked down with the correct visible copy and retry-button availability.

Relevant files
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreenTest.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- handoff/validation-report.md

Constraints
- Keep the block test-focused unless a tiny production adjustment is required to expose an existing summary-card state for testing
- Preserve current dashboard behavior, strings, semantics, and retry action dispatching
- Reuse the existing dashboard state builder helpers instead of introducing a new test harness

What not to change
- Do not modify DashboardViewModel, repository/domain/data code, or navigation wiring
- Do not broaden into unrelated dashboard visual changes beyond tiny testability fixes
- Do not touch unrelated workspace changes under `.idea/` or `.vscode/`

Done criteria
- Compose UI tests cover the summary card ready state copy when cached currency metadata is available
- Compose UI tests cover the refreshing states, including cached-data refresh messaging and retry button suppression while sync is in progress
- Compose UI tests cover the unavailable/failure state copy and retry button visibility when sync is not in progress
- The smallest meaningful androidTest compile validation is attempted again and recorded in `handoff/validation-report.md`

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The next block is still bounded to dashboard presentation tests, but it needs careful assertions across several closely related summary-card states.
