Next block name
BLOCK 39 - Automation Harness Run Manifest Completion Status

Objective
Extend the run-level automation manifest so it also reports whether the harness completed all requested blocks or stopped early, along with the number of blocks actually executed, without changing the existing per-block summary schema or Codex prompt flow.

Relevant files
- run-blocks.sh
- docs/automation-harness.md

Constraints
- Keep the work limited to the automation harness and its documentation
- Preserve the current fixed-count-only behavior, fresh session behavior, and existing Codex implementation prompt exactly
- Keep the handoff-driven reasoning-effort resolution from `handoff/next-block.md`
- Preserve the existing per-block summary artifact format
- Keep the run manifest simple and machine-readable
- Keep compatibility with the currently installed Codex CLI surface in this workspace
- Use the smallest meaningful verification for the shell-script scope and update `handoff/validation-report.md`

What not to change
- Do not modify Android app production code, tests, Gradle build logic, or repository architecture
- Do not change AGENTS.md, docs/blueprint.md, docs/official-docs.md, or existing validation scripts
- Do not add run-until-complete behavior, background daemons, or multi-process orchestration

Done criteria
- The run manifest includes a simple completion status for the harness run
- The run manifest includes how many blocks were actually executed before exit
- Documentation reflects the new manifest fields and distinguishes requested block count from executed block count
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This remains a localized shell-script and documentation update with straightforward verification.
