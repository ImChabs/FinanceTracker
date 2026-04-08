[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Get-RepoRoot {
    Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
}

function Get-ProtectedBranch {
    if ($env:GIT_AUTOMATION_PROTECTED_BRANCH) {
        return $env:GIT_AUTOMATION_PROTECTED_BRANCH
    }

    return 'main'
}

function Get-CurrentBranch {
    $branch = git symbolic-ref --quiet --short HEAD 2>$null
    if ($LASTEXITCODE -ne 0) {
        return $null
    }

    return $branch.Trim()
}

function Test-ProtectedBranch {
    param(
        [Parameter(Mandatory = $true)]
        [string]$BranchName
    )

    return $BranchName -eq (Get-ProtectedBranch)
}

function Test-DocsOnlyPath {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    return (
        $Path -like '.agents/*' -or
        $Path -like '.githooks/*' -or
        $Path -like 'docs/*' -or
        $Path -like 'handoff/*' -or
        $Path -like 'handoff-history/*' -or
        $Path -like 'scripts/git/*' -or
        $Path -eq 'README.md'
    )
}

function Test-AndroidTestOnlyPath {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    return $Path -like 'app/src/androidTest/*'
}

function Test-NeedsCompileForPath {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    return (
        $Path -like 'app/src/main/*' -or
        $Path -eq 'app/build.gradle.kts' -or
        $Path -eq 'build.gradle.kts' -or
        $Path -eq 'settings.gradle.kts' -or
        $Path -eq 'gradle.properties' -or
        $Path -like 'gradle/*' -or
        $Path -eq 'gradlew' -or
        $Path -eq 'gradlew.bat' -or
        $Path -eq 'scripts/validate-compile.sh' -or
        $Path -eq 'scripts/validate-compile.ps1' -or
        $Path -eq 'scripts/validate-unit-tests.sh' -or
        $Path -eq 'scripts/validate-unit-tests.ps1'
    )
}

function Test-NeedsUnitTestsForPath {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    return (
        $Path -like 'app/src/test/*' -or
        $Path -like 'app/src/main/java/*/domain/*' -or
        $Path -like 'app/src/main/java/*/data/*' -or
        $Path -like 'app/src/main/java/*ViewModel.kt'
    )
}

function Invoke-BranchProtectionCheck {
    & powershell -ExecutionPolicy Bypass -File (Join-Path (Get-RepoRoot) 'scripts/validate-branch-protection-checks.ps1')
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }
}

function Invoke-CompileCheck {
    param(
        [string]$GradleTask
    )

    $scriptPath = Join-Path (Get-RepoRoot) 'scripts/validate-compile.ps1'
    if ($GradleTask) {
        & powershell -ExecutionPolicy Bypass -File $scriptPath -GradleTask $GradleTask
    }
    else {
        & powershell -ExecutionPolicy Bypass -File $scriptPath
    }

    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }
}

function Invoke-UnitTestCheck {
    & powershell -ExecutionPolicy Bypass -File (Join-Path (Get-RepoRoot) 'scripts/validate-unit-tests.ps1')
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }
}

function Write-GitAutomationInfo {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Message
    )

    Write-Host "[git-automation] $Message"
}
