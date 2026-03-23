Next block name
BLOCK 52 - CI Required Checks Alignment

Objective
After the first successful GitHub Actions run, align repository branch protection with the new Android CI jobs so pull requests require the same compile, unit-test, and lint checks that are now automated.

Relevant files
- .github/workflows/android-ci.yml
- handoff/validation-report.md

Constraints
- Keep the scope focused on CI governance and any tiny workflow-name adjustments needed for stable required-check labels
- Do not broaden into release, signing, deployment, emulator, or secret management work
- Preserve the current compile, unit-test, and lint task selection unless a real CI run proves a narrower or safer task is required

What not to change
- Do not add CD, publishing, or branch-protection automation
- Do not add new quality tools such as detekt or ktlint unless they are introduced intentionally in a separate block
- Do not change app feature behavior

Done criteria
- The first live GitHub Actions run identifies the exact status-check labels emitted by the workflow
- Repository branch protection is updated manually to require those checks for pull requests into the protected branch
- Any needed workflow-name or job-name stabilization is identified clearly if the live run exposes a naming mismatch

## Execution Recommendation
- Recommended reasoning effort: low
- Rationale: The next step should be mostly operational follow-through after the workflow has already landed and run once.
