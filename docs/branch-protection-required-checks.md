# Branch Protection Required Checks

## Purpose

This repository uses the Android CI workflow to gate pull requests into the protected branch.
Use this runbook after the first successful GitHub Actions run so branch protection requires the same checks that the workflow emits.

## Required check labels

Confirm that the latest successful run from `.github/workflows/android-ci.yml` exposes these exact check names:

- `Android CI - Assemble Debug`
- `Android CI - Unit Tests`
- `Android CI - Lint Debug`

If GitHub displays a different label for any check, update this document and `handoff/next-block.md` before changing branch protection so the repo stays aligned with the live status checks.

## Manual application steps

1. Open the repository on GitHub.
2. Go to `Settings` -> `Branches`.
3. Open the branch protection rule for the protected branch, or create one if it does not exist yet.
4. Enable the setting that requires status checks to pass before merging.
5. Add these required checks:
   - `Android CI - Assemble Debug`
   - `Android CI - Unit Tests`
   - `Android CI - Lint Debug`
6. Save the branch protection rule.
7. Open or refresh a pull request targeting the protected branch and confirm the three required checks appear as merge requirements.

## Verification checklist

- A successful Android CI run exists on GitHub for the current workflow definition.
- The emitted check labels match the three labels listed above.
- The protected branch requires those same checks before merge.
- A pull request shows those checks in the merge box without any unexpected duplicate or stale check names.
