[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$workflowFile = Join-Path $repoRoot '.github/workflows/android-ci.yml'
$runbookFile = Join-Path $repoRoot 'docs/branch-protection-required-checks.md'

if (-not (Test-Path $workflowFile)) {
    throw "Could not find workflow file at $workflowFile"
}

if (-not (Test-Path $runbookFile)) {
    throw "Could not find runbook file at $runbookFile"
}

Write-Host 'Validating branch-protection required-check labels against workflow job names'
$tempScript = Join-Path ([System.IO.Path]::GetTempPath()) ("validate-branch-protection-checks-" + [guid]::NewGuid().ToString() + ".py")
$script = @'
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
'@

Set-Content -LiteralPath $tempScript -Value $script -NoNewline
try {
    python $tempScript $workflowFile $runbookFile
}
finally {
    if (Test-Path $tempScript) {
        Remove-Item -LiteralPath $tempScript -Force
    }
}
exit $LASTEXITCODE
