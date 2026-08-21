@echo off
chcp 65001 > nul
setlocal

echo ===================================================
echo   Háztartás Kalkulátor Backend Indítása (Spring Boot)
echo ===================================================

if "%JAVA_HOME%"=="" (
    if exist "C:\Program Files\JetBrains\DataGrip 2026.2.4\jbr" (
        set "JAVA_HOME=C:\Program Files\JetBrains\DataGrip 2026.2.4\jbr"
    )
)

if "%JAVA_HOME%"=="" (
    echo [FIGYELEM] JAVA_HOME nincs beállítva. Próbálkozás a rendszer java parancsával...
    set "JAVACMD=java"
) else (
    set "JAVACMD=%JAVA_HOME%\bin\java.exe"
)

if not exist "%~dp0target\haztartas-kalkulator-1.0.0.jar" (
    echo [INFO] JAR állomány fordítása és csomagolása...
    call "%~dp0mvnw.cmd" clean package -DskipTests
)

echo.
echo [INFO] Backend indítása a lefordított JAR állományból...
echo [TIPP] Ha a PostgreSQL jelszavad nem 'postgres', a backend/src/main/resources/application.yml fájlban vagy környezeti változóban megadhatod.
echo.

"%JAVACMD%" -jar "%~dp0target\haztartas-kalkulator-1.0.0.jar" %*
pause
