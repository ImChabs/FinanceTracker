[CmdletBinding()]
param(
    [ValidateSet('structure', 'complete')]
    [string]$Mode = 'structure'
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$reportFile = Join-Path $repoRoot 'handoff/branch-protection-application-report.md'

if (-not (Test-Path $reportFile)) {
    throw "Could not find report file at $reportFile"
}

Write-Host "Validating branch-protection application report ($Mode mode)"

$script = @'
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
'@

python -c $script $reportFile $Mode
exit $LASTEXITCODE
