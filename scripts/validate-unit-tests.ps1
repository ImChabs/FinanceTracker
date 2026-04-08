[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$gradleWrapper = Join-Path $repoRoot 'gradlew.bat'
$repoGradleHome = Join-Path $repoRoot '.gradle-local'
$repoAndroidHome = Join-Path $repoRoot '.android-local'

if (-not (Test-Path $gradleWrapper)) {
    throw "Could not find gradle wrapper at $gradleWrapper"
}

Push-Location $repoRoot
try {
    if (-not (Test-Path $repoGradleHome)) {
        New-Item -ItemType Directory -Path $repoGradleHome | Out-Null
    }

    if (-not (Test-Path $repoAndroidHome)) {
        New-Item -ItemType Directory -Path $repoAndroidHome | Out-Null
    }

    $env:GRADLE_USER_HOME = $repoGradleHome
    $env:ANDROID_USER_HOME = $repoAndroidHome
    Write-Host "Running targeted unit-test validation: :app:testDebugUnitTest"
    & $gradleWrapper ':app:testDebugUnitTest'
    exit $LASTEXITCODE
}
finally {
    Pop-Location
}
