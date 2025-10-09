#!/bin/bash

# Pokemon Project - Local CI Test Script
# This script helps test GitHub Actions workflows locally

echo "🎮 Pokemon Project - Local CI Test"
echo "=================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to run command and check result
run_check() {
    local cmd="$1"
    local desc="$2"
    
    echo -e "\n${YELLOW}🔍 $desc${NC}"
    echo "Running: $cmd"
    
    if eval "$cmd"; then
        echo -e "${GREEN}✅ $desc - PASSED${NC}"
        return 0
    else
        echo -e "${RED}❌ $desc - FAILED${NC}"
        return 1
    fi
}

# Check if we're in the right directory
if [ ! -f "build.gradle.kts" ]; then
    echo -e "${RED}❌ Error: Not in Pokemon project root directory${NC}"
    exit 1
fi

# Create local.properties if it doesn't exist
if [ ! -f "local.properties" ]; then
    echo "📝 Creating local.properties..."
    if [ -n "$ANDROID_HOME" ]; then
        echo "sdk.dir=$ANDROID_HOME" > local.properties
    else
        echo -e "${YELLOW}⚠️  Warning: ANDROID_HOME not set. Please set it manually in local.properties${NC}"
    fi
fi

echo -e "\n🚀 Starting local CI checks...\n"

# 1. Gradle Build
run_check "./gradlew build --stacktrace" "Gradle Build"
build_result=$?

# 2. Unit Tests
run_check "./gradlew test --stacktrace" "Unit Tests"
test_result=$?

# 3. Lint Check
run_check "./gradlew lintDebug --stacktrace" "Android Lint"
lint_result=$?

# 4. Ktlint Check
run_check "./gradlew ktlintCheck --stacktrace" "Kotlin Lint"
ktlint_result=$?

# 5. Dependency Updates Check
run_check "./gradlew dependencyUpdates --stacktrace" "Dependency Updates"
deps_result=$?

# 6. Assemble Debug APK
run_check "./gradlew assembleDebug --stacktrace" "Debug APK Build"
apk_result=$?

# Summary
echo -e "\n🏁 CI Check Summary"
echo "===================="

total_checks=6
passed_checks=0

[ $build_result -eq 0 ] && ((passed_checks++)) || echo -e "${RED}❌ Gradle Build${NC}"
[ $test_result -eq 0 ] && ((passed_checks++)) || echo -e "${RED}❌ Unit Tests${NC}"
[ $lint_result -eq 0 ] && ((passed_checks++)) || echo -e "${RED}❌ Android Lint${NC}"
[ $ktlint_result -eq 0 ] && ((passed_checks++)) || echo -e "${RED}❌ Kotlin Lint${NC}"
[ $deps_result -eq 0 ] && ((passed_checks++)) || echo -e "${RED}❌ Dependency Updates${NC}"
[ $apk_result -eq 0 ] && ((passed_checks++)) || echo -e "${RED}❌ Debug APK Build${NC}"

if [ $passed_checks -eq $total_checks ]; then
    echo -e "\n${GREEN}🎉 All checks passed! ($passed_checks/$total_checks)${NC}"
    echo -e "${GREEN}✅ Your code is ready for GitHub Actions!${NC}"
    exit 0
else
    echo -e "\n${YELLOW}⚠️  Some checks failed ($passed_checks/$total_checks)${NC}"
    echo -e "${YELLOW}Please fix the issues before pushing to GitHub${NC}"
    exit 1
fi
