@echo off
setlocal
if "%JAVA_HOME%"=="" (
    if exist "C:\Program Files\JetBrains\DataGrip 2026.2.4\jbr" (
        set "JAVA_HOME=C:\Program Files\JetBrains\DataGrip 2026.2.4\jbr"
    )
)
set "MAVEN_EXE=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6\bin\mvn.cmd"
if exist "%MAVEN_EXE%" (
    call "%MAVEN_EXE%" %*
) else (
    "%JAVA_HOME%\bin\java.exe" "-Dmaven.multiModuleProjectDirectory=%~dp0." -cp "%~dp0.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
)
