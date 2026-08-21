param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$Arguments
)

if (-not $env:JAVA_HOME) {
    if (Test-Path "C:\Program Files\JetBrains\DataGrip 2026.2.4\jbr") {
        $env:JAVA_HOME = "C:\Program Files\JetBrains\DataGrip 2026.2.4\jbr"
        $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
    }
}

$mavenExe = "$env:USERPROFILE\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6\bin\mvn.cmd"
if (Test-Path $mavenExe) {
    & $mavenExe @Arguments
} else {
    & "$env:JAVA_HOME\bin\java.exe" "-Dmaven.multiModuleProjectDirectory=$PSScriptRoot" -cp "$PSScriptRoot\.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain @Arguments
}
