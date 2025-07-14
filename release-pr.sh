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

# Sync local develop
echo "🔄 Checking out and updating $DEVELOP_BRANCH..."
git checkout $DEVELOP_BRANCH
git pull origin $DEVELOP_BRANCH
git fetch origin $MAIN_BRANCH

# Get current version
CURRENT_VERSION=$(grep "^version *= *" $VERSION_FILE | cut -d'"' -f2)
RELEASE_VERSION="${CURRENT_VERSION/-SNAPSHOT/}"

if [[ "$CURRENT_VERSION" == "$RELEASE_VERSION" ]]; then
  echo "❌ Version is already a release: $RELEASE_VERSION"
  exit 1
fi

echo "🔧 Preparing release for version: $RELEASE_VERSION"

# --- Step 1: Create release branch and PR to master ---
RELEASE_BRANCH="release/$RELEASE_VERSION"

# Delete local branch if it already exists
git branch -D $RELEASE_BRANCH 2>/dev/null || true
git checkout -b $RELEASE_BRANCH

# Remove -SNAPSHOT from version
sed -i '' "s/^version *= *\".*\"/version = \"$RELEASE_VERSION\"/" $VERSION_FILE
git commit -am "🔖 Release $RELEASE_VERSION"
git push -u origin $RELEASE_BRANCH

# Create PR to master
gh pr create --base $MAIN_BRANCH --head $RELEASE_BRANCH \
  --title "🔖 Release $RELEASE_VERSION" \
  --body "Release version $RELEASE_VERSION to $MAIN_BRANCH"

echo "✅ Release PR created: $RELEASE_BRANCH → $MAIN_BRANCH"

# Cleanup release branch locally
git checkout $DEVELOP_BRANCH
git branch -D $RELEASE_BRANCH
echo "🧹 Deleted local branch: $RELEASE_BRANCH"

# --- Step 2: Create backport branch and PR to develop ---
IFS='.' read -r MAJOR MINOR PATCH <<< "$RELEASE_VERSION"
NEXT_PATCH=$((PATCH + 1))
NEXT_VERSION="${MAJOR}.${MINOR}.${NEXT_PATCH}-SNAPSHOT"
BACKPORT_BRANCH="release/backport-$NEXT_VERSION"

# Delete local branch if it already exists
git branch -D $BACKPORT_BRANCH 2>/dev/null || true
git checkout -b $BACKPORT_BRANCH $MAIN_BRANCH

# Set next snapshot version
sed -i '' "s/^version *= *\".*\"/version = \"$NEXT_VERSION\"/" $VERSION_FILE
git commit -am "🔄 Prepare for next development version: $NEXT_VERSION"
git push -u origin $BACKPORT_BRANCH

# Create PR back to develop
gh pr create --base $DEVELOP_BRANCH --head $BACKPORT_BRANCH \
  --title "🔄 Bump version to $NEXT_VERSION" \
  --body "Backport next snapshot version after $RELEASE_VERSION"

echo "✅ Backport PR created: $BACKPORT_BRANCH → $DEVELOP_BRANCH"

# Cleanup backport branch locally
git checkout $DEVELOP_BRANCH
git branch -D $BACKPORT_BRANCH
echo "🧹 Deleted local branch: $BACKPORT_BRANCH"

echo "🎉 All done."
