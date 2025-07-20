#!/bin/bash
# This script publishes the release to github
# @Todo Automate this later with a GitHub Action
# Note: Always update version here and change notes before running this script
set -e

git checkout master
git pull origin master
./gradlew clean build

# Extract version from build.gradle.kts
VERSION=$(grep "^version\s*=" build.gradle.kts | sed -E 's/version\s*=\s*"(.*)"/\1/')

# Remove -SNAPSHOT if present for release tag
RELEASE_VERSION="${VERSION/-SNAPSHOT/}"

# Construct title and notes
TITLE="v$RELEASE_VERSION"
NOTES="Initial release with basic functionality todo task management"

# Path to JAR
JAR_PATH="./build/libs/cli-todoer-*-all.jar"

# Create GitHub release
gh release create "v$RELEASE_VERSION" $JAR_PATH --title "v$TITLE" --notes "$NOTES"
