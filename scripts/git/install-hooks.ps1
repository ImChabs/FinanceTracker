[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
Push-Location $repoRoot
try {
    git config core.hooksPath .githooks
    Write-Host 'Configured git core.hooksPath to .githooks'
}
finally {
    Pop-Location
}
