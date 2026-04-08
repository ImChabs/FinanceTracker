---
name: commit-and-push
description: Review the current Git state, stage only the intended files, rely on the repo-owned hooks and validations, then create one safe commit and push the current branch.
---

# Commit And Push

## Purpose

- Create exactly one safe commit from the current repository state.
- Push the current branch without bypassing local verification.
- Keep Git automation aligned with the repo-owned hook scripts under `scripts/git/`.

## Inputs

- Read `AGENTS.md` first for repository workflow and validation expectations.
- Inspect `git status --short`, `git branch --show-current`, and `git remote -v`.
- Read `handoff/next-block.md` when it is relevant to the change set.
- Treat `.idea/inspectionProfiles/Project_Default.xml`, `local.properties`, `build/`, `.gradle/`, `.gradle-local/`, `.kotlin/`, and `automation-logs/` as blocked machine-local paths.

## Workflow

1. Confirm the current branch is not detached.
2. Refuse to commit directly on the protected branch unless the user explicitly instructs it.
3. Inspect the full worktree and identify unrelated or suspicious changes before staging.
4. Stage only the intended files. Do not use `git add .`.
5. If `handoff/next-block.md` is staged, ensure exactly one new `handoff-history/<next-prefix>-*.md` file is staged too.
6. Choose a commit message that is short, specific, and scoped to the actual change.
7. Commit normally and let `pre-commit` run. Do not use `--no-verify`.
8. Push the current branch. If no upstream exists, use `git push --set-upstream origin <branch>`.
9. Let `pre-push` run. Do not use `--no-verify`.
10. Report what was committed, what validation ran, and whether the push succeeded.

## Safety Rules

- Never use `git add .` or `git commit -a`.
- Never use `--no-verify`, `--force`, or `--force-with-lease` unless the user explicitly requests it.
- Never push directly to the protected branch during normal work.
- Stop if the branch is behind its upstream and let the user choose the rebase or merge strategy.
- Stop if the staged set includes blocked machine-local files, especially `.idea/inspectionProfiles/Project_Default.xml`.
- Prefer the repo-owned installation command for hooks:
  - PowerShell: `powershell -ExecutionPolicy Bypass -File .\scripts\git\install-hooks.ps1`
  - Shell: `sh scripts/git/install-hooks.sh`

## Validation Expectations

- Trust the repo-owned hooks as the default automation path once installed.
- If hooks are not installed, run the same repo scripts manually before commit or push:
  - `powershell -ExecutionPolicy Bypass -File .\scripts\git\run-pre-commit.ps1`
  - `powershell -ExecutionPolicy Bypass -File .\scripts\git\run-pre-push.ps1`
- Do not broaden validation beyond the repo-owned hook logic unless the user explicitly asks.
