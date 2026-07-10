param(
    [Parameter(Mandatory = $true)]
    [string]$ModulePath
)

$root = Split-Path -Parent $PSScriptRoot
$pom = Join-Path $root $ModulePath
$pom = Join-Path $pom "pom.xml"

if (-not (Test-Path $pom)) {
    throw "pom.xml not found for module path: $ModulePath"
}

mvn -f $pom spring-boot:run