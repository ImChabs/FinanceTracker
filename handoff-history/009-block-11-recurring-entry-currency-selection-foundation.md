Next block name
BLOCK 11 - Recurring Entry Currency Selection Foundation

Objective
Extend recurring entries to store a selected currency code and expose a simple currency picker in the create/edit flow using cached currency metadata, without introducing exchange-rate conversion or changing dashboard totals beyond preserving the existing amount display behavior.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/recurring/domain/model/RecurringEntry.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/data/local/
- app/src/main/java/com/example/newfinancetracker/feature/recurring/data/repository/
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/form/
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/create/
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/
- app/src/main/java/com/example/newfinancetracker/feature/currency/domain/repository/CurrencyMetadataRepository.kt
- app/src/test/java/com/example/newfinancetracker/feature/recurring/

Constraints
- Use cached currency metadata already exposed by the repository; do not trigger new sync behavior from the form screens
- Keep the create/edit UX simple with one currency choice per recurring entry and a reasonable default when metadata is unavailable
- Preserve local-first behavior so the form remains usable even when no currency metadata is cached
- Avoid exchange-rate math, converted dashboard totals, or per-entry conversion displays in this block
- Choose the smallest meaningful verification for the recurring-entry currency selection flow

What not to change
- Do not add background sync, workers, or currency settings screens
- Do not redesign the dashboard beyond what is already implemented
- Do not introduce historical rates, base-currency preferences, or live conversion logic
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- Recurring entry domain/data models persist a currency code cleanly
- Create and edit flows let the user choose a currency from cached metadata, with a usable fallback when metadata is missing
- Existing recurring-entry save/edit behavior continues to work with the new field
- Focused tests cover the new persistence and form behavior
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: high
- Rationale: The next step crosses Room, repository mapping, and create/edit form state, so it needs careful coordination to avoid regressions.
