#!/bin/bash
# This script publishes the release to github
# @Todo Automate this later with a GitHub Action
# Note: Always update version here and change notes before running this script
set -e

# Be on the master branch before release
git checkout master
git pull origin master

#Build the ouput files
./gradlew clean build

# Extract version from build.gradle.kts
VERSION=$(grep -E '^\s*version' build.gradle.kts | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')

# extract release notes from CHANGELOG.md
CHANGELOG_NOTES=$(awk -v version="$VERSION" '
  BEGIN { in_block=0 }
  $0 ~ "^## \\[" version "\\]" { in_block=1; next }
  in_block && $0 ~ "^## " { exit }
  in_block { print }
' CHANGELOG.md)

# Path to JAR
JAR_PATH="./build/libs/cli-todoer-*-all.jar"

# Show previous steps
echo "Ready to release version v$VERSION with JAR: $JAR_PATH"
echo "Release notes: $CHANGELOG_NOTES"
read -p "Do you want to publish this release? (y/N): " CONFIRM
if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
  echo "Release cancelled."
  exit 0
fi

# Create GitHub release
gh release create "v$VERSION" $JAR_PATH --title "v$VERSION" --notes "$NOTES" --target master