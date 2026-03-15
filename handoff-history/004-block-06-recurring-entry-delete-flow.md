Next block name
BLOCK 06 - Recurring Entry Delete Flow

Objective
Allow the user to delete an existing recurring entry from the edit flow with a clear confirmation step and persist the removal through the local repository.

Relevant files
- app/src/main/java/com/example/newfinancetracker/feature/recurring/domain/repository/RecurringEntryRepository.kt
- app/src/main/java/com/example/newfinancetracker/feature/recurring/presentation/edit/
- app/src/main/java/com/example/newfinancetracker/feature/dashboard/presentation/DashboardScreen.kt
- app/src/main/res/values/strings.xml

Constraints
- Keep this block focused on deleting an existing recurring entry from the edit flow
- Reuse the existing repository delete method instead of adding a parallel persistence path
- Make the destructive action explicit with a confirmation step or equivalent clear guardrail
- Preserve the current dashboard overview plus recurring-entry create and edit flows
- Prefer the smallest meaningful verification for the affected `app` scope

What not to change
- Do not add swipe-to-delete, bulk actions, or dashboard-only delete affordances yet
- Do not redesign the recurring entry form or dashboard layout beyond the minimum delete affordance needed
- Do not add remote/API work
- Do not modify AGENTS.md, docs/blueprint.md, or docs/official-docs.md

Done criteria
- The edit flow exposes a delete action for an existing entry
- The user must confirm before the entry is deleted
- Deleting persists through the repository and returns to the previous screen
- The dashboard no longer shows the deleted entry after returning
- The missing-entry path remains reasonable after delete-flow changes
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: medium
- Rationale: This block is a contained destructive-flow addition, but it still needs careful state handling and a clear UX guardrail.
