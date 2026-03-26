#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

mode="${1:-structure}"
repo_root=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
report_file="$repo_root/handoff/branch-protection-application-report.md"
workflow_file="$repo_root/.github/workflows/android-ci.yml"
runbook_file="$repo_root/docs/branch-protection-required-checks.md"

if [[ ! -f "$report_file" ]]; then
  printf 'Could not find report file at %s\n' "$report_file" >&2
  exit 1
fi

if [[ ! -f "$workflow_file" ]]; then
  printf 'Could not find workflow file at %s\n' "$workflow_file" >&2
  exit 1
fi

if [[ ! -f "$runbook_file" ]]; then
  printf 'Could not find runbook file at %s\n' "$runbook_file" >&2
  exit 1
fi

case "$mode" in
  structure|complete)
    ;;
  *)
    printf 'Unsupported mode: %s\n' "$mode" >&2
    printf 'Usage: bash scripts/validate-branch-protection-application-report.sh [structure|complete]\n' >&2
    exit 1
    ;;
esac

printf 'Validating branch-protection application report (%s mode)\n' "$mode"

python3 - "$report_file" "$workflow_file" "$runbook_file" "$mode" <<'PY'
from pathlib import Path
import re
import sys

report_file = Path(sys.argv[1])
workflow_file = Path(sys.argv[2])
runbook_file = Path(sys.argv[3])
mode = sys.argv[4]
report = report_file.read_text(encoding="utf-8")
workflow = workflow_file.read_text(encoding="utf-8")
runbook = runbook_file.read_text(encoding="utf-8")

required_sections = [
    "# Branch Protection Application Report",
    "Status",
    "Preflight",
    "GitHub Actions verification",
    "Branch protection update",
    "Pull request verification",
    "Notes",
]

missing_sections = [section for section in required_sections if section not in report]
if missing_sections:
    print("Missing report sections:", ", ".join(missing_sections), file=sys.stderr)
    sys.exit(1)

if mode == "complete":
    disallowed_tokens = [
        "Completion status: pending",
        "<fill in",
        "<pass|fail>",
        "<success|other>",
        "<yes|no>",
    ]
    present_tokens = [token for token in disallowed_tokens if token in report]
    if present_tokens:
        print("Report is not complete. Replace remaining placeholders/todo values:", file=sys.stderr)
        for token in present_tokens:
            print(f"- {token}", file=sys.stderr)
        sys.exit(1)

    workflow_labels = re.findall(r"^\s*name:\s*(Android CI - .+?)\s*$", workflow, flags=re.MULTILINE)
    runbook_labels = re.findall(r"- `(Android CI - .+?)`", runbook)[:3]

    observed_match = re.search(
        r"Observed check labels:\n((?:  - .+\n){3})",
        report,
        flags=re.MULTILINE,
    )
    configured_match = re.search(
        r"Required status checks configured:\n((?:  - .+\n){3})",
        report,
        flags=re.MULTILINE,
    )

    if observed_match is None or configured_match is None:
        print(
            "Report must contain exactly three observed check labels and three configured checks.",
            file=sys.stderr,
        )
        sys.exit(1)

    observed_labels = re.findall(r"  - (.+)", observed_match.group(1))
    configured_labels = re.findall(r"  - (.+)", configured_match.group(1))

    if observed_labels != runbook_labels:
        print("Observed check labels do not match the runbook labels.", file=sys.stderr)
        print("Observed:", observed_labels, file=sys.stderr)
        print("Runbook:", runbook_labels, file=sys.stderr)
        sys.exit(1)

    if configured_labels != runbook_labels:
        print("Configured required checks do not match the runbook labels.", file=sys.stderr)
        print("Configured:", configured_labels, file=sys.stderr)
        print("Runbook:", runbook_labels, file=sys.stderr)
        sys.exit(1)

    if workflow_labels != runbook_labels:
        print("Workflow job names do not match the runbook labels.", file=sys.stderr)
        print("Workflow:", workflow_labels, file=sys.stderr)
        print("Runbook:", runbook_labels, file=sys.stderr)
        sys.exit(1)

print("Branch-protection application report validation passed.")
PY
