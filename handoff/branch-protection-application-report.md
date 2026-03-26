# Branch Protection Application Report

Status
- Completion status: pending
- Protected branch: `main`
- Checked by: <fill in name>
- Checked on: <fill in YYYY-MM-DD>

Preflight
- Repo-local label validation command run: <fill in command>
- Preflight result: <pass|fail>

GitHub Actions verification
- Workflow run URL: <fill in URL>
- Workflow run conclusion: <success|other>
- Observed check labels:
  - <fill in label 1>
  - <fill in label 2>
  - <fill in label 3>
- Labels matched the runbook exactly: <yes|no>

Branch protection update
- Branch rule URL or settings location: <fill in URL or note>
- Required status checks configured:
  - <fill in configured check 1>
  - <fill in configured check 2>
  - <fill in configured check 3>
- Require status checks before merging enabled: <yes|no>

Pull request verification
- Pull request URL: <fill in URL>
- Merge box showed the expected required checks: <yes|no>
- Unexpected duplicate or stale checks present: <yes|no>

Notes
- Record any mismatch between GitHub-emitted labels and the runbook here before changing `docs/branch-protection-required-checks.md` or `handoff/next-block.md`.
