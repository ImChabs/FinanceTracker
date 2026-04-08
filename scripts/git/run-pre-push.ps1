[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

. (Join-Path $PSScriptRoot 'common.ps1')

$currentBranch = Get-CurrentBranch
if ($currentBranch -and (Test-ProtectedBranch -BranchName $currentBranch) -and $env:GIT_ALLOW_PROTECTED_PUSH -ne '1') {
    throw "Direct pushes to protected branch $(Get-ProtectedBranch) are blocked. Open a PR instead."
}

$changedPaths = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::Ordinal)
$stdinLines = @()
while (($line = [Console]::In.ReadLine()) -ne $null) {
    if (-not [string]::IsNullOrWhiteSpace($line)) {
        $stdinLines += $line
    }
}

foreach ($line in $stdinLines) {
    $parts = $line -split ' '
    if ($parts.Count -lt 4) {
        continue
    }

    $localSha = $parts[1]
    $remoteRef = $parts[2]
    $remoteSha = $parts[3]

    if ($localSha -eq '0000000000000000000000000000000000000000') {
        continue
    }

    $remoteBranch = $remoteRef -replace '^refs/heads/', ''
    if ((Test-ProtectedBranch -BranchName $remoteBranch) -and $env:GIT_ALLOW_PROTECTED_PUSH -ne '1') {
        throw "Push target $remoteBranch is protected. Open a PR instead."
    }

    if ($remoteSha -eq '0000000000000000000000000000000000000000') {
        $delta = @(git diff-tree --no-commit-id --name-only -r $localSha)
    }
    else {
        $delta = @(git diff --name-only $remoteSha $localSha)
    }

    foreach ($path in $delta) {
        if (-not [string]::IsNullOrWhiteSpace($path)) {
            [void]$changedPaths.Add($path)
        }
    }
}

if ($changedPaths.Count -eq 0) {
    Write-GitAutomationInfo 'No push delta detected'
    exit 0
}

$needsBranchProtection = $false
$needsCompile = $false
$needsAndroidTestCompile = $false
$needsUnitTests = $false
$docsOnly = $true

foreach ($path in $changedPaths) {
    if (-not (Test-DocsOnlyPath -Path $path)) {
        $docsOnly = $false
    }

    if ($path -eq '.github/workflows/android-ci.yml' -or $path -eq 'docs/branch-protection-required-checks.md') {
        $needsBranchProtection = $true
    }

    if (Test-AndroidTestOnlyPath -Path $path) {
        $needsAndroidTestCompile = $true
    }

    if (Test-NeedsCompileForPath -Path $path) {
        $needsCompile = $true
    }

    if (Test-NeedsUnitTestsForPath -Path $path) {
        $needsUnitTests = $true
    }
}

if ($needsBranchProtection) {
    Invoke-BranchProtectionCheck
}

if ($docsOnly -and -not $needsBranchProtection) {
    Write-GitAutomationInfo 'Docs-only push detected; skipping Gradle validation'
    exit 0
}

if ($needsCompile) {
    Invoke-CompileCheck
}
elseif ($needsAndroidTestCompile) {
    Invoke-CompileCheck -GradleTask ':app:compileDebugAndroidTestKotlin'
}

if ($needsUnitTests) {
    Invoke-UnitTestCheck
}

Write-GitAutomationInfo 'Pre-push checks passed'
