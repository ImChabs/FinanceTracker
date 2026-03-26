# Validation Report

Current block
- Name: BLOCK 56 - Branch Protection Application Report Validator
- Scope: Add repo-local validation scripts for the manual branch-protection application report so the GitHub-side follow-through has a repeatable structural check now and a complete-mode validation step after the report is filled out.

Loop 1
- Validation target: `bash scripts/validate-branch-protection-application-report.sh`
- Underlying command: `bash scripts/validate-branch-protection-application-report.sh`
- Why this target: The changed scope is a new report-validation helper and runbook update, so the smallest meaningful verification is running the helper in its default structural mode against the current pending report template.
- Final status: passed
- Attempts used: 1/3
- Run 1: Passed. `bash scripts/validate-branch-protection-application-report.sh` confirmed the current report includes the expected sections in structural mode.
- Run 2: Not used.
- Run 3: Not used.
- In-scope fixes applied: Added Bash and PowerShell report-validation scripts and updated the runbook so the eventual manual GitHub follow-through ends with a local complete-mode validation step.
- Outstanding issues: The actual GitHub Actions run inspection and branch-protection update still require manual GitHub access outside this workspace.

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
- Documented required-check labels for branch protection remain:
- `Android CI - Assemble Debug`
- `Android CI - Unit Tests`
- `Android CI - Lint Debug`
- The runbook now includes both the preflight label check and a final complete-mode validation command for the manual application report.
