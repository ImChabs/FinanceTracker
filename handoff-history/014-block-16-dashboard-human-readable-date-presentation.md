Next block name
BLOCK 16 - Dashboard Human-Readable Date Presentation

Objective
Improve dashboard readability by replacing raw ISO date strings in the upcoming payments section and recurring entry cards with a user-friendly display format, while preserving existing sorting, storage, and validation behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardState.kt
- app/src/main/res/values/strings.xml
- app/src/test/java/com/example/newfinancetracker/feature/dashboard/presentation/
- app/src/androidTest/java/com/example/newfinancetracker/feature/dashboard/presentation/

Constraints
- Preserve the existing ISO-backed persistence model and do not change stored date values
- Keep current upcoming-payment sorting behavior unchanged
- Only change dashboard date presentation where dates are already shown to the user
- Handle invalid or legacy date strings gracefully instead of crashing; fallback to the raw stored text if formatting cannot be resolved
- Choose the smallest meaningful verification for the presentation behavior you change

What not to change
- Do not modify recurring create/edit form inputs, validation rules, or submission payloads
- Do not change Room schema, DAO contracts, or repository interfaces for dates
- Do not redesign dashboard sections beyond the date presentation adjustments needed for readability
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Upcoming payments show user-friendly dates instead of raw ISO strings when the stored date is valid
- Recurring entry cards show user-friendly next payment dates instead of raw ISO strings when the stored date is valid
- Invalid or legacy date strings remain visible as-is rather than disappearing or causing errors
- Existing dashboard sorting behavior remains unchanged
- Focused dashboard tests cover the new date presentation behavior
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: The work is still tightly scoped, but it needs careful formatting and fallback handling across both state and Compose presentation.
