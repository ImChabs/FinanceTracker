Next block name
BLOCK 33 - Automation Harness Failure Summary Metadata

Objective
Extend the fixed-count Codex WSL automation harness with a small, machine-readable per-block summary artifact that records block number, UTC timestamp, exit status, and the paths of the JSONL log and last assistant message, without changing the existing handoff or validation workflow.

Relevant files
- run-blocks.sh
- docs/automation-harness.md

Constraints
- Keep the work limited to the new automation harness and its documentation
- Preserve the current fixed-count-only behavior and existing Codex prompt exactly
- Do not redesign the repository handoff, validation, or archive flow
- Keep any new output format simple and easy to audit
- Use the smallest meaningful verification for the shell-script scope and update `handoff/validation-report.md`

What not to change
- Do not modify Android app production code, tests, Gradle build logic, or repository architecture
- Do not change AGENTS.md, docs/blueprint.md, docs/official-docs.md, or existing validation scripts
- Do not add run-until-complete behavior, background daemons, or multi-process orchestration

Done criteria
- Each block execution also writes a small machine-readable summary artifact alongside the existing logs
- The summary includes enough information to identify block order, success/failure, and artifact locations
- Documentation reflects the new summary artifact without broadening the harness scope
- The smallest meaningful verification is attempted

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: This is a small shell-script enhancement with localized behavior and straightforward verification.
