#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

mode="${1:-structure}"
repo_root=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
report_file="$repo_root/handoff/branch-protection-application-report.md"

if [[ ! -f "$report_file" ]]; then
  printf 'Could not find report file at %s\n' "$report_file" >&2
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

python3 - "$report_file" "$mode" <<'PY'
from pathlib import Path
import sys

report_file = Path(sys.argv[1])
mode = sys.argv[2]
report = report_file.read_text(encoding="utf-8")

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

print("Branch-protection application report validation passed.")
PY
