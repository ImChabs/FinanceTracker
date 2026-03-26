#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

repo_root=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
workflow_file="$repo_root/.github/workflows/android-ci.yml"
runbook_file="$repo_root/docs/branch-protection-required-checks.md"

if [[ ! -f "$workflow_file" ]]; then
  printf 'Could not find workflow file at %s\n' "$workflow_file" >&2
  exit 1
fi

if [[ ! -f "$runbook_file" ]]; then
  printf 'Could not find runbook file at %s\n' "$runbook_file" >&2
  exit 1
fi

printf 'Validating branch-protection required-check labels against workflow job names\n'

python3 - "$workflow_file" "$runbook_file" <<'PY'
from pathlib import Path
import re
import sys

workflow_file = Path(sys.argv[1])
runbook_file = Path(sys.argv[2])

workflow_text = workflow_file.read_text(encoding="utf-8")
runbook_text = runbook_file.read_text(encoding="utf-8")

workflow_labels = re.findall(r"^\s*name:\s*(Android CI - .+?)\s*$", workflow_text, flags=re.MULTILINE)
runbook_labels = re.findall(r"- `(Android CI - .+?)`", runbook_text)[:3]

print("Workflow labels:")
for label in workflow_labels:
    print(f"- {label}")

print("Runbook labels:")
for label in runbook_labels:
    print(f"- {label}")

if workflow_labels != runbook_labels:
    print("Mismatch detected between workflow job names and documented required checks.", file=sys.stderr)
    sys.exit(1)

print("Branch-protection required-check labels match the workflow job names.")
PY
