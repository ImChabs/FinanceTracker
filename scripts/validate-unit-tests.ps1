[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$gradleWrapper = Join-Path $repoRoot 'gradlew.bat'

if (-not (Test-Path $gradleWrapper)) {
    throw "Could not find gradle wrapper at $gradleWrapper"
}

Push-Location $repoRoot
try {
    Write-Host "Running targeted unit-test validation: :app:testDebugUnitTest"
    & $gradleWrapper ':app:testDebugUnitTest'
    exit $LASTEXITCODE
}
finally {
    Pop-Location
}