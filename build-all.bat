@echo off
echo Building FluxTiers for all Minecraft versions...
echo.

set VERSIONS=1.19.2

for %%v in (%VERSIONS%) do (
    echo ========================================
    echo Building for Minecraft %%v
    echo ========================================
    call gradlew.bat clean build -PmcVersion=%%v
    if errorlevel 1 (
        echo Failed to build for Minecraft %%v
        exit /b 1
    )
    echo.
)

echo ========================================
echo All builds completed successfully!
echo ========================================
echo Built jars are in build\libs\
