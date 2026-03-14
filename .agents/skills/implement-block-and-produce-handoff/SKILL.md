---
name: implement-block-and-produce-handoff
description: Implement one bounded development block in this repository, verify the affected scope, and produce the next-block handoff. Use when working one block per Codex chat/thread and a clean implementation-to-handoff workflow is needed.
---

# Implement Block and Produce Handoff

## Source of Truth

- Read `AGENTS.md` first.
- Treat `AGENTS.md` as the authoritative guide for repository conventions, architecture, structure, naming, state handling, and verification expectations.
- Read `docs/blueprint.md` if it exists before defining block scope.
- Use `docs/blueprint.md` as the source of truth for product goal, feature roadmap, high-level application scope, and long-term project direction.
- Read `handoff/next-block.md` if it exists.
- Use `handoff/next-block.md` as the source of truth for the immediate next block.
- Treat `handoff/next-block.md` as the only live source of truth for the immediate next block unless the user explicitly says otherwise.
- Treat `handoff-history/` as archival only and never as the source of truth unless the user explicitly requests historical lookup.
- Resolve the current block by combining repository rules from `AGENTS.md`, product direction from `docs/blueprint.md`, and immediate scope from `handoff/next-block.md`.
- Do not restate or override repository rules from `AGENTS.md`.

## Goal

- Complete exactly one development block per chat/thread.
- Implement only the requested scope, plus small adjacent fixes required to make the block correct and verifiable.
- Leave a concise handoff for the next block at `handoff/next-block.md`.
- Archive each completed handoff as a separate history file under `handoff-history/`.

## Workflow

1. Read `AGENTS.md`.
2. Read the current block request, `docs/blueprint.md` if it exists, and `handoff/next-block.md` if it exists.
3. Define the exact block scope before editing by combining repository rules, product direction, and immediate next-block guidance:
   - What must change
   - What can stay untouched
   - What adjacent fix is acceptable only if required to complete the block correctly
4. Implement the block.
5. Verify the smallest meaningful affected scope.
6. If verification fails because of block changes, fix issues that are in scope or are a small required adjacent correction.
7. Summarize the result clearly.
8. Overwrite `handoff/next-block.md` with the next-block handoff.
9. Write a second archival copy of that same handoff into `handoff-history/` as a new file without overwriting prior history files.

## Scope Control

- Stay inside the requested block.
- Avoid unrelated refactors, cleanup, file moves, or architecture churn.
- Prefer the correct solution over the superficially smallest diff.
- Make a small adjacent change only when needed to finish the block cleanly or resolve a problem introduced by the block work.
- Call out any limitation, deferred work, or unresolved issue explicitly.

## Verification

- Always attempt verification.
- Choose the smallest meaningful verification for the affected module, target, or task.
- Avoid `clean`, full builds, and expensive project-wide verification unless clearly needed.
- If verification cannot be completed, say so explicitly.
- If verification exposes issues caused by the block changes, attempt to fix them when they remain within scope or are a small required adjacent fix.

## Handoff Files

- Create the `handoff` directory first if it does not already exist.
- Create the `handoff-history` directory first if it does not already exist.
- Always create or overwrite `handoff/next-block.md`.
- `handoff/next-block.md` remains the only live source of truth for the immediate next block.
- Always write a second archival copy of the generated handoff into `handoff-history/`.
- `handoff-history/` is archival only and must not be used as the source of truth unless explicitly requested.
- Never overwrite or delete prior files in `handoff-history/`.
- Use one file per archived handoff, not a cumulative history file.
- Name archive files with a zero-padded numeric prefix followed by a short slug, for example `001-block-01-project-foundation-and-app-shell.md`.
- Continue numbering from the highest existing numeric prefix already present in `handoff-history/`.
- Keep it short, specific, and actionable.
- Include an `Execution Recommendation` section in every generated handoff.
- Include:
  - `Next block name`
  - `Objective`
  - `Relevant files`
  - `Constraints`
  - `What not to change`
  - `Done criteria`
- Format the execution recommendation as:
  - `## Execution Recommendation`
  - `- Recommended reasoning effort: <low|medium|high|xhigh>`
  - `- Rationale: <brief explanation>`
- Base the next block on the current state of the codebase after your changes, not on the original request.

## Execution Recommendation Guidance

- Every generated handoff must include an `Execution Recommendation` section.
- Allowed values are exactly:
  - `low`
  - `medium`
  - `high`
  - `xhigh`
- Choose the recommendation by evaluating the expected complexity of the next block.
- Use the rubric below as guidance, not as a rigid formula.
- Always include a brief one-line rationale.
- Decision rubric:
  - `low`: small, mechanical, low-risk work
  - `medium`: default for normal implementation blocks
  - `high`: meaningful ambiguity, multi-layer coordination, or harder verification
  - `xhigh`: hardest cases with deep coordination, difficult debugging, or major tradeoffs

## Final Response Format

Use this structure at the end of the block:

## Block summary
- State what was implemented and what was intentionally left out.

## Files created/modified
- List the files changed for this block using repository-relative paths.

## Verification performed
- State exactly what was run.
- State whether it passed, failed and was fixed, or could not be completed.

## Known limitations / pending items
- List remaining risks, follow-ups, or blockers if any.

## Handoff file written
- Confirm that `handoff/next-block.md` was written or overwritten.
