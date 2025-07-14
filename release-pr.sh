#!/bin/bash

set -e

VERSION_FILE="build.gradle.kts"
MAIN_BRANCH="master"
DEVELOP_BRANCH="develop"

# Ensure clean working directory
if ! git diff --quiet; then
  echo "❌ Uncommitted changes found. Please commit or stash them first."
  exit 1
fi

# Fetch latest
git checkout $DEVELOP_BRANCH
git pull origin $DEVELOP_BRANCH
git fetch origin $MAIN_BRANCH

# Extract current version from build.gradle.kts
CURRENT_VERSION=$(grep "^version *= *" $VERSION_FILE | cut -d'"' -f2)
RELEASE_VERSION="${CURRENT_VERSION/-SNAPSHOT/}"

if [[ "$CURRENT_VERSION" == "$RELEASE_VERSION" ]]; then
  echo "❌ Version is already a release: $RELEASE_VERSION"
  exit 1
fi

echo "🔧 Releasing version: $RELEASE_VERSION"

# --- Step 1: Create release branch ---
RELEASE_BRANCH="release/$RELEASE_VERSION"
git checkout -b $RELEASE_BRANCH

# Remove -SNAPSHOT
sed -i '' "s/^version *= *\".*\"/version = \"$RELEASE_VERSION\"/" $VERSION_FILE
git commit -am "🔖 Release $RELEASE_VERSION"
git push -u origin $RELEASE_BRANCH

# Create PR to master
gh pr create --base $MAIN_BRANCH --head $RELEASE_BRANCH \
  --title "🔖 Release $RELEASE_VERSION" \
  --body "Merging release $RELEASE_VERSION into $MAIN_BRANCH"

echo "✅ Release PR created: $RELEASE_BRANCH → $MAIN_BRANCH"

# --- Step 2: Create backport branch with incremented version ---
IFS='.' read -r MAJOR MINOR PATCH <<< "${RELEASE_VERSION}"
NEXT_PATCH=$((PATCH + 1))
NEXT_VERSION="${MAJOR}.${MINOR}.${NEXT_PATCH}-SNAPSHOT"

BACKPORT_BRANCH="release/backport-$NEXT_VERSION"
git checkout -b $BACKPORT_BRANCH $MAIN_BRANCH

sed -i '' "s/^version *= *\".*\"/version = \"$NEXT_VERSION\"/" $VERSION_FILE
git commit -am "🔄 Bump version to $NEXT_VERSION"
git push -u origin $BACKPORT_BRANCH

# Create PR to develop
gh pr create --base $DEVELOP_BRANCH --head $BACKPORT_BRANCH \
  --title "🔄 Bump version to $NEXT_VERSION" \
  --body "Backport next development version after $RELEASE_VERSION release"

echo "✅ Backport PR created: $BACKPORT_BRANCH → $DEVELOP_BRANCH"
