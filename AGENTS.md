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

## Planning Inputs
- For product goals, feature scope, roadmap context, and overall application direction, consult `docs/blueprint.md` if present.
- For the immediate next implementation step, consult `handoff/next-block.md` if present.
- When framework or library behavior is uncertain, prefer official documentation and consult `docs/official-docs.md` if present.

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
- Avoid `clean` and full rebuilds unless truly necessary.
- Do not leave compile errors caused by the change.
- If verification could not be completed, state it explicitly.
- Add tests when they provide real value.
- Do not add ceremonial tests.
- Choose the test type that best matches the actual risk.
