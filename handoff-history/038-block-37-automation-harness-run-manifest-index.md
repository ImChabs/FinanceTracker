Next block name
BLOCK 37 - Automation Harness Run Manifest Index

Objective
Extend the fixed-count Codex WSL automation harness with a small run-level manifest that records the shared run timestamp, requested block count, and the paths of all per-block summary artifacts produced during that run, without changing the existing per-block prompt, handoff flow, or summary schema.

Relevant files
- run-blocks.sh
- docs/automation-harness.md

Constraints
- Keep the work limited to the automation harness and its documentation
- Preserve the current fixed-count-only behavior, fresh session behavior, and existing Codex implementation prompt exactly
- Keep the handoff-driven reasoning-effort resolution from `handoff/next-block.md`
- Preserve the per-block summary artifact format and keep any run-level manifest similarly simple and machine-readable
- Keep compatibility with the currently installed Codex CLI surface in this workspace
- Do not redesign the repository handoff, validation, or archive flow
- Use the smallest meaningful verification for the shell-script scope and update `handoff/validation-report.md`

What not to change
- Do not modify Android app production code, tests, Gradle build logic, or repository architecture
- Do not change AGENTS.md, docs/blueprint.md, docs/official-docs.md, or existing validation scripts
- Do not add run-until-complete behavior, background daemons, or multi-process orchestration

Done criteria
- Each harness run also writes one simple machine-readable manifest for the run
- The manifest includes the run timestamp, requested block count, and references to the per-block summary artifact paths generated during that run
- Documentation reflects the run manifest and its relationship to the per-block summaries without broadening the harness scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is another localized shell-script/documentation enhancement with straightforward behavior and focused verification.
