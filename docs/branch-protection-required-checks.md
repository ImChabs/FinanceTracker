# Branch Protection Required Checks

## Purpose

This repository uses the Android CI workflow to gate pull requests into the protected branch.
Use this runbook after the first successful GitHub Actions run so branch protection requires the same checks that the workflow emits.

Before changing branch protection, run the repo-local label validation command for your shell so the documented required checks still match the workflow job names:

- WSL/Bash: `bash scripts/validate-branch-protection-checks.sh`
- PowerShell/Windows: `.\scripts\validate-branch-protection-checks.ps1`

## Required check labels

Confirm that the latest successful run from `.github/workflows/android-ci.yml` exposes these exact check names:

- `Android CI - Assemble Debug`
- `Android CI - Unit Tests`
- `Android CI - Lint Debug`

If GitHub displays a different label for any check, update this document and `handoff/next-block.md` before changing branch protection so the repo stays aligned with the live status checks.

## Manual application steps

1. Run the repo-local branch-protection label validation script for your shell and confirm it passes.
2. Open the repository on GitHub.
3. Go to `Settings` -> `Branches`.
4. Open the branch protection rule for the protected branch, or create one if it does not exist yet.
5. Enable the setting that requires status checks to pass before merging.
6. Add these required checks:
   - `Android CI - Assemble Debug`
   - `Android CI - Unit Tests`
   - `Android CI - Lint Debug`
7. Save the branch protection rule.
8. Open or refresh a pull request targeting the protected branch and confirm the three required checks appear as merge requirements.

## Verification checklist

- The repo-local branch-protection label validation script passes against the current workflow and runbook files.
- A successful Android CI run exists on GitHub for the current workflow definition.
- The emitted check labels match the three labels listed above.
- The protected branch requires those same checks before merge.
- A pull request shows those checks in the merge box without any unexpected duplicate or stale check names.
