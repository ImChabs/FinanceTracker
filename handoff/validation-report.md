# Validation Report

Current block
- Name: Branch protection required-check runbook
- Scope: Add a repository runbook that records the exact Android CI required-check labels and the manual GitHub branch-protection steps needed to apply them.

Loop 1
- Validation target: `python3` workflow/doc label consistency check
- Underlying command: `python3 - <<'PY' ... compare labels in docs/branch-protection-required-checks.md against .github/workflows/android-ci.yml ... PY`
- Why this target: The changed scope is documentation for manual branch-protection setup, so the smallest meaningful verification is confirming the documented required-check labels still match the workflow's emitted job names exactly.
- Final status: passed
- Attempts used: 1/3
- Run 1: Passed. Extracted the required-check labels from `docs/branch-protection-required-checks.md` and confirmed they match the job names in `.github/workflows/android-ci.yml`: `Android CI - Assemble Debug`, `Android CI - Unit Tests`, and `Android CI - Lint Debug`.
- Run 2: Not used.
- Run 3: Not used.
- In-scope fixes applied: Added `docs/branch-protection-required-checks.md` with the exact labels, manual GitHub settings path, and a verification checklist for the protected branch rule.
- Outstanding issues: The actual GitHub branch-protection change still must be applied outside this workspace after a successful live Actions run is available.

Loop 2
- Validation target: <optional second validation script>
- Underlying command: <optional gradle command>
- Why this target: <why a second loop was needed>
- Final status: not_run
- Attempts used: 0/3
- Run 1: Not used.
- Run 2: Not used.
- Run 3: Not used.
- In-scope fixes applied: None recorded.
- Outstanding issues: None recorded.

Notes
- Documented required-check labels for branch protection are now:
- `Android CI - Assemble Debug`
- `Android CI - Unit Tests`
- `Android CI - Lint Debug`
