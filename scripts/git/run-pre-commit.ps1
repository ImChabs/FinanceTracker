[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

. (Join-Path $PSScriptRoot 'common.ps1')

Write-GitAutomationInfo 'Running pre-commit checks'
git diff --cached --check
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$stagedFiles = @(git diff --cached --name-only --diff-filter=ACMR)
if (-not $stagedFiles -or $stagedFiles.Count -eq 0) {
    Write-GitAutomationInfo 'No staged changes to validate'
    exit 0
}

foreach ($path in $stagedFiles) {
    if ([string]::IsNullOrWhiteSpace($path)) {
        continue
    }

    if (
        $path -eq 'local.properties' -or
        $path -like 'automation-logs/*' -or
        $path -like '.gradle/*' -or
        $path -like '.gradle-local/*' -or
        $path -like '.kotlin/*' -or
        $path -like 'build/*' -or
        $path -like '*/build/*'
    ) {
        throw "Blocked staged path: $path"
    }

    if ($path -eq '.idea/inspectionProfiles/Project_Default.xml') {
        throw "Blocked staged IDE path: $path"
    }
}

$nextBlockStaged = @(git diff --cached --name-only -- handoff/next-block.md)
if ($nextBlockStaged.Count -gt 0) {
    $newHistoryFiles = @(git diff --cached --name-only --diff-filter=A -- handoff-history | Where-Object { $_ })
    if ($newHistoryFiles.Count -ne 1) {
        throw 'Staging handoff/next-block.md requires exactly one new handoff-history file.'
    }

    $newHistoryFile = $newHistoryFiles[0]
    if ($newHistoryFile -notmatch '^handoff-history/([0-9]{3})-') {
        throw "New handoff-history file must use a zero-padded numeric prefix: $newHistoryFile"
    }

    $newPrefix = [int]$matches[1]
    $headHistoryFiles = @(git ls-tree -r --name-only HEAD -- handoff-history | Where-Object { $_ -match '^handoff-history/([0-9]{3})-' })
    $highestPrefix = 0
    foreach ($historyPath in $headHistoryFiles) {
        if ($historyPath -match '^handoff-history/([0-9]{3})-') {
            $prefix = [int]$matches[1]
            if ($prefix -gt $highestPrefix) {
                $highestPrefix = $prefix
            }
        }
    }

    if ($newPrefix -ne ($highestPrefix + 1)) {
        $expectedPrefix = '{0:D3}' -f ($highestPrefix + 1)
        throw "Expected new handoff-history prefix $expectedPrefix but found $('{0:D3}' -f $newPrefix)."
    }
}

$branchProtectionFiles = @(git diff --cached --name-only -- .github/workflows/android-ci.yml docs/branch-protection-required-checks.md)
if ($branchProtectionFiles.Count -gt 0) {
    Invoke-BranchProtectionCheck
}

Write-GitAutomationInfo 'Pre-commit checks passed'
