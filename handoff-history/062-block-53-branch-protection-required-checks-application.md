Next block name
BLOCK 53 - Branch Protection Required Checks Application

Objective
Apply the stabilized Android CI required-check labels in GitHub branch protection for the protected branch after confirming the first successful workflow run exposes those exact check names.

Relevant files
- .github/workflows/android-ci.yml
- handoff/validation-report.md

Constraints
- Keep the scope focused on GitHub branch-protection configuration and confirming the emitted check labels from the live run
- Do not broaden into release, signing, deployment, emulator, or secret management work
- Preserve the stabilized required-check labels unless a real GitHub Actions run shows GitHub emitting a different status-check name

What not to change
- Do not add CD, publishing, or branch-protection automation
- Do not add new quality tools such as detekt or ktlint unless they are introduced intentionally in a separate block
- Do not change app feature behavior

Done criteria
- The first live GitHub Actions run is checked against the stabilized labels in `.github/workflows/android-ci.yml`
- Repository branch protection is updated manually to require `Android CI - Assemble Debug`, `Android CI - Unit Tests`, and `Android CI - Lint Debug`
- Any mismatch between GitHub’s emitted labels and the workflow job names is documented clearly for a follow-up block if needed

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: The remaining work is a narrow manual GitHub settings update plus a quick check that the live run uses the stabilized labels.
