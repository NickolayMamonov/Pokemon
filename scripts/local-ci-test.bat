@echo off
:: Pokemon Project - Local CI Test Script (Windows)
:: This script helps test GitHub Actions workflows locally

echo 🎮 Pokemon Project - Local CI Test
echo ==================================

:: Check if we're in the right directory
if not exist "build.gradle.kts" (
    echo ❌ Error: Not in Pokemon project root directory
    exit /b 1
)

:: Create local.properties if it doesn't exist
if not exist "local.properties" (
    echo 📝 Creating local.properties...
    if defined ANDROID_HOME (
        echo sdk.dir=%ANDROID_HOME% > local.properties
    ) else (
        echo ⚠️  Warning: ANDROID_HOME not set. Please set it manually in local.properties
    )
)

echo.
echo 🚀 Starting local CI checks...
echo.

set total_checks=6
set passed_checks=0

:: 1. Gradle Build
echo 🔍 Gradle Build
echo Running: gradlew build --stacktrace
call gradlew build --stacktrace
if %errorlevel% equ 0 (
    echo ✅ Gradle Build - PASSED
    set /a passed_checks+=1
) else (
    echo ❌ Gradle Build - FAILED
)

echo.

:: 2. Unit Tests
echo 🔍 Unit Tests
echo Running: gradlew test --stacktrace
call gradlew test --stacktrace
if %errorlevel% equ 0 (
    echo ✅ Unit Tests - PASSED
    set /a passed_checks+=1
) else (
    echo ❌ Unit Tests - FAILED
)

echo.

:: 3. Lint Check
echo 🔍 Android Lint
echo Running: gradlew lintDebug --stacktrace
call gradlew lintDebug --stacktrace
if %errorlevel% equ 0 (
    echo ✅ Android Lint - PASSED
    set /a passed_checks+=1
) else (
    echo ❌ Android Lint - FAILED
)

echo.

:: 4. Ktlint Check
echo 🔍 Kotlin Lint
echo Running: gradlew ktlintCheck --stacktrace
call gradlew ktlintCheck --stacktrace
if %errorlevel% equ 0 (
    echo ✅ Kotlin Lint - PASSED
    set /a passed_checks+=1
) else (
    echo ❌ Kotlin Lint - FAILED
)

echo.

:: 5. Dependency Updates Check
echo 🔍 Dependency Updates
echo Running: gradlew dependencyUpdates --stacktrace
call gradlew dependencyUpdates --stacktrace
if %errorlevel% equ 0 (
    echo ✅ Dependency Updates - PASSED
    set /a passed_checks+=1
) else (
    echo ❌ Dependency Updates - FAILED
)

echo.

:: 6. Assemble Debug APK
echo 🔍 Debug APK Build
echo Running: gradlew assembleDebug --stacktrace
call gradlew assembleDebug --stacktrace
if %errorlevel% equ 0 (
    echo ✅ Debug APK Build - PASSED
    set /a passed_checks+=1
) else (
    echo ❌ Debug APK Build - FAILED
)

echo.
echo 🏁 CI Check Summary
echo ====================

if %passed_checks% equ %total_checks% (
    echo.
    echo 🎉 All checks passed! ^(%passed_checks%/%total_checks%^)
    echo ✅ Your code is ready for GitHub Actions!
    exit /b 0
) else (
    echo.
    echo ⚠️  Some checks failed ^(%passed_checks%/%total_checks%^)
    echo Please fix the issues before pushing to GitHub
    exit /b 1
)
