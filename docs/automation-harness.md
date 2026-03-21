# Automation Harness

This repository includes a small WSL-oriented Bash wrapper for running a fixed number of sequential Codex implementation blocks.

## Usage

From the repository root in WSL:

```bash
./run-blocks.sh 3
```

Optional artifact mode:

```bash
./run-blocks.sh --save-logs 3
```

The script:
- requires `handoff/next-block.md` to exist before starting and before each new block
- starts a fresh `codex exec` session for every block
- uses the repository handoff workflow already in place
- resolves the reasoning effort for each block from the current `handoff/next-block.md`
- expects the per-block validation workflow inside those WSL sessions to use the bash-native validation scripts in `scripts/`
- relies on the installed Codex configuration for approval policy while keeping the compatible `--sandbox workspace-write` override
- keeps terminal output minimal during normal runs
- keeps runtime artifacts disabled by default for a cleaner working tree
- enables runtime artifacts only when `--save-logs` is passed explicitly

## Validation Environment Alignment

Because the harness runs WSL/Bash sessions, the Level 1 validation workflow should use the bash-native repo scripts from those sessions:

- `bash scripts/validate-compile.sh`
- `bash scripts/validate-unit-tests.sh`

`bash scripts/validate-compile.sh` defaults to `:app:compileDebugKotlin` and also accepts an explicit Gradle task override such as `:app:compileDebugAndroidTestKotlin` when the smallest meaningful verification is an `androidTest` or instrumentation compile target.

The existing PowerShell validation scripts remain available for non-WSL Codex sessions on Windows, but the harness path is expected to stay bash-native end to end.

## Archive History Guardrail

The harness now checks `handoff-history/` before the run starts and after every successful block.

- It stops safely if duplicate numeric archive prefixes already exist.
- It expects each completed block to add exactly one new archive file.
- It expects that new archive file to use the next numeric prefix after the current maximum.

## Reasoning effort resolution

For each block, the script reads the live handoff source of truth at `handoff/next-block.md` and looks for the existing repository convention:

`- Recommended reasoning effort: <value>`

Resolution behavior:
- `low`, `medium`, and `high` are passed through directly to Codex CLI as `model_reasoning_effort`
- `xhigh` is mapped down to `high` for CLI execution
- missing or invalid values fall back conservatively to `medium`

The value is resolved again before every block, so if one block updates the handoff recommendation for the next block, the following `codex exec` run uses the new recommendation automatically.

Important wording:
- the printed and recorded detected reasoning effort is the value parsed from `handoff/next-block.md`
- the printed and recorded Codex config override is the exact `-c` request the harness passes to `codex exec`
- this is a trace of what the harness detected and requested, not a stronger guarantee about internal runtime behavior than the current Codex CLI surface exposes

## CLI compatibility

This harness is aligned to the currently installed `codex exec` surface in this workspace.

- It does not pass `--ask-for-approval`
- Approval policy is inherited from the existing Codex configuration
- The script still passes `--sandbox workspace-write`, which is supported by the installed CLI and keeps workspace-write execution explicit

## Runtime artifacts

By default, the harness does not write per-block runtime artifacts.
In default mode, the underlying `codex exec` stdout and stderr streams are suppressed rather than persisted, so no temporary transcript capture is kept on disk.

When `--save-logs` is enabled, the script writes artifacts under `automation-logs/`:
- `automation-logs/<utc-timestamp>-manifest.json` stores a run-level manifest for the full harness invocation
- `automation-logs/<utc-timestamp>-block-<n>.jsonl` stores the machine-readable Codex event stream for each block
- `automation-logs/last-messages/<utc-timestamp>-block-<n>.md` stores the last assistant message for each block
- `automation-logs/summaries/<utc-timestamp>-block-<n>.json` stores a small per-block run manifest artifact

Each run manifest JSON records:
- `run_timestamp_utc`
- `requested_block_count`
- `executed_block_count`
- `completion_status`
- `summary_artifact_paths`

`requested_block_count` is the original harness input. `executed_block_count` is how many block runs actually produced per-block summary artifacts before the harness exited. `completion_status` is `completed` when all requested blocks ran successfully and `stopped_early` when the harness wrote the manifest after an early failure.

`summary_artifact_paths` lists the per-block summary JSON files produced during that run in block order, so downstream tooling can discover the run-level artifact first and then follow those references to the unchanged per-block summaries.

Each per-block run manifest JSON records:
- `block_number`
- `timestamp_utc`
- `detected_reasoning_effort`
- `codex_config_override_requested`
- `result`
- `exit_code`
- `jsonl_log_path`
- `last_message_path`

The per-block run manifest is written for both successful and failed block executions so the harness leaves a compact audit artifact even when it stops early on a failure. The run manifest is also written on both successful and failed runs, distinguishes requested count from executed count, reports whether the run completed, and references every per-block manifest artifact produced before the harness exits.

All runtime artifacts advertised as `.json` are emitted as valid JSON, including `codex_config_override_requested` when the recorded `-c` value itself contains quotes.

If `--save-logs` is not enabled, none of those runtime artifact files are created.

## Terminal output

During execution, the harness prints:
- one live heartbeat line per running block:
  - `[|] Block X/Y | elapsed HH:MM:SS | effort <value>`
- one final line per finished block:
  - success: `[ok] Block X/Y | success | duration HH:MM:SS | effort <value>`
  - failure: `[x] Block X/Y | fail | duration HH:MM:SS | effort <value> | error code <n>`
- one compact run summary line at exit

If token usage is not reliably available from the current Codex CLI surface, it is omitted rather than guessed.

When `--save-logs` is enabled, runtime artifact files are written, but normal terminal output still stays compact.
Default mode does not persist temporary transcript files.

If `handoff/next-block.md` is missing before the run starts, or disappears before a later block begins, the harness stops safely and still prints an exit summary.

Interpretation notes:
- the printed effort value reflects what the harness parsed from `handoff/next-block.md`
- the live heartbeat confirms that the block is still running
- the heartbeat is intentionally indeterminate and does not claim percentage completion

## Current limitations

This first version is intentionally minimal and easy to audit.

- Only fixed-count execution is supported
- Run-until-complete is not supported
- The script assumes `codex` is already installed, authenticated, and available in the WSL shell PATH
- Validation of block contents remains the responsibility of the per-block Codex execution and the existing repository workflow
