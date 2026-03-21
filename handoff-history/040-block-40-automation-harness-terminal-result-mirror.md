Next block name
BLOCK 40 - Automation Harness Run Manifest Terminal Result Mirror

Objective
Extend the run-level automation manifest so it also mirrors the terminal per-block outcome at the run level, including which block index ended the run and whether that terminal block succeeded or failed, without changing the existing per-block summary schema or Codex prompt flow.

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
- The run manifest includes the block index that produced the terminal run outcome
- The run manifest includes a simple terminal block result mirror at the run level
- Documentation explains the new run-level terminal outcome fields without implying any per-block summary schema change
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is another localized harness metadata addition with straightforward documentation and shell verification.
