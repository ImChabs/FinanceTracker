# AGENTS.md

## Stack
- Native Android app
- Kotlin
- Jetpack Compose
- Material 3
- MVVM + UDF
- Navigation 3
- Coroutines
- Flow
- Hilt
- Room
- Retrofit 3

## Instruction Ownership
- `AGENTS.md` defines durable repository rules: architecture, structure, naming, state handling, dependency/design constraints, and verification expectations.
- `docs/blueprint.md` defines product goals, scope, roadmap context, and out-of-scope boundaries.
- `handoff/next-block.md` defines the immediate next implementation block.
- `handoff-history/` is archival only and should not be treated as the live source of truth unless historical lookup is explicitly needed.
- `docs/official-docs.md` is a selective reference index for official framework and library guidance when behavior is uncertain.

## Planning Inputs
- For product goals, feature scope, roadmap context, and overall application direction, consult `docs/blueprint.md` if present.
- For the immediate next implementation step, consult `handoff/next-block.md` if present.
- When framework or library behavior is uncertain, prefer official documentation and consult `docs/official-docs.md` if present.
- Read only the files needed for the current task and avoid loading archival history or broad documentation unless it is relevant to the work.

## Project Structure
- Organize code primarily by feature.
- A feature may contain `presentation`, `domain`, `data`, and `navigation` when complexity justifies it.
- Put truly shared or reusable code in `core`.
- Do not move code into `core` unless it is used across multiple features or is clearly cross-cutting.

## Layer Rules
- `presentation`: screens, screen roots, viewmodels, UI state, UI actions, UI effects.
- `domain`: domain models, use cases, repository contracts.
- `data`: repository implementations, local/remote data sources, Room, Retrofit, DTOs, entities, mappers.
- `domain` must not depend on `data` or `presentation`.
- `presentation` consumes `domain`.
- `data` implements `domain` contracts.

## Screen Conventions
- If a screen needs a ViewModel, use both `XScreen` and `XScreenRoot`.
- Root naming must follow `DashboardScreenRoot`, not `DashboardRootScreen`.
- Keep `Screen` and `ScreenRoot` in the same screen file.
- Typical structure for a complex screen:
  - `XScreen.kt`
  - `XViewModel.kt`
  - `XState.kt`
  - `XAction.kt`
  - `XEffect.kt`
- `State` should be a `data class`.
- `Action` should be a `sealed interface`.
- `Effect` should be a `sealed interface`.
- Previews belong to the presentational `Screen`.
- `ScreenRoot` should not have a preview.
- Very simple screens may omit the root when no ViewModel is needed.

## State Management
- Use `StateFlow` for UI state.
- Use `SharedFlow` for one-off events and effects.
- Collect state in Compose with the appropriate collection API.
- Prefer state hoisting.
- Presentational screens should receive `state` and `actions`.
- Keep meaningful business logic out of composables.

## Use Cases
- Use cases are optional, not mandatory.
- Introduce them when they add real value through business logic, orchestration, reuse, clarity, or testability.
- Do not add trivial pass-through use cases that only delegate to a repository.

## Dependency And Design Rules
- Prefer official, stable Android libraries.
- Do not add dependencies without a real need.
- Avoid unnecessary wrappers and abstractions.
- Prefer the right solution over the superficially smallest change.
- Do not weaken architecture just to minimize diff size.
- Avoid overengineering.
- Do not touch unrelated files unless necessary for the task.

## Verification And Testing
- Always try to verify changes before considering the task complete.
- Use the smallest meaningful verification for the affected scope.
- Prefer focused module-level or target-level verification over full project builds.
- For targeted compile verification in this single-module app, prefer the repo-local validation script for the current shell:
  - PowerShell/Windows: `.\scripts\validate-compile.ps1`
  - WSL/Bash: `bash scripts/validate-compile.sh`
- The default targeted compile task is `:app:compileDebugKotlin`.
- When the changed scope is limited to `androidTest` or instrumentation/Compose UI test sources, use the same compile validation script with `:app:compileDebugAndroidTestKotlin`.
- Use the repo-local unit-test validation script for the current shell when domain/data logic changes or when adding/updating unit tests:
  - PowerShell/Windows: `.\scripts\validate-unit-tests.ps1`
  - WSL/Bash: `bash scripts/validate-unit-tests.sh`
- The targeted unit-test task remains `:app:testDebugUnitTest`.
- Use targeted instrumentation or Compose UI tests only when UI behavior changes warrant them.
- Avoid `clean` and full rebuilds unless truly necessary.
- Do not leave compile errors caused by the change.
- If verification could not be completed, state it explicitly.
- Add tests when they provide real value.
- Do not add ceremonial tests.
- Choose the test type that best matches the actual risk.
