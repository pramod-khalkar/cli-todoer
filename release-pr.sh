#!/bin/bash

set -e

VERSION_FILE="build.gradle.kts"
MAIN_BRANCH="master"
DEVELOP_BRANCH="develop"

# -----------------------
# 🆘 Help Section
# -----------------------
show_help() {
  echo ""
  echo "Usage: ./release-pr.sh <release-version> <next-snapshot-version>"
  echo ""
  echo "Example:"
  echo "  ./release-pr.sh 1.0.1 1.1.0"
  echo ""
  echo "This will:"
  echo "  - Create PR to master with version 1.0.1"
  echo "  - Create PR to develop with version 1.1.0-SNAPSHOT"
  echo ""
}

if [[ "$1" == "--help" || "$1" == "-h" ]]; then
  show_help
  exit 0
fi

# -----------------------
# ✅ Argument Validation
# -----------------------
if [[ -z "$1" || -z "$2" ]]; then
  echo "❌ Missing arguments."
  show_help
  exit 1
fi

RELEASE_VERSION="$1"
NEXT_VERSION="$2-SNAPSHOT"

# -----------------------
# ✅ Ensure clean working directory
# -----------------------
if ! git diff --quiet; then
  echo "❌ Uncommitted changes found. Please commit or stash them first."
  exit 1
fi

# -----------------------
# 🔄 Sync develop branch
# -----------------------
echo "🔄 Checking out and updating $DEVELOP_BRANCH..."
git checkout $DEVELOP_BRANCH
git pull origin $DEVELOP_BRANCH
git fetch origin $MAIN_BRANCH

# -----------------------
# 🔍 Check current version
# -----------------------
CURRENT_VERSION=$(grep "^version *= *" "$VERSION_FILE" | cut -d'"' -f2)

if [[ "$CURRENT_VERSION" != *-SNAPSHOT ]]; then
  echo "❌ Current version in $VERSION_FILE is not a snapshot: $CURRENT_VERSION"
  echo "Expected format: X.Y.Z-SNAPSHOT"
  exit 1
fi

# -----------------------
# 🛠 Step 1: Create release branch and PR to master
# -----------------------
RELEASE_BRANCH="release/$RELEASE_VERSION"

git branch -D "$RELEASE_BRANCH" 2>/dev/null || true
git checkout -b "$RELEASE_BRANCH"

# Set release version (no -SNAPSHOT)
sed -i '' "s/^version *= *\".*\"/version = \"$RELEASE_VERSION\"/" "$VERSION_FILE"
git commit -am "🔖 Release $RELEASE_VERSION"
git push -u origin "$RELEASE_BRANCH"

gh pr create --base "$MAIN_BRANCH" --head "$RELEASE_BRANCH" \
  --title "🔖 Release $RELEASE_VERSION" \
  --body "Release version $RELEASE_VERSION to $MAIN_BRANCH"

echo "✅ Release PR created: $RELEASE_BRANCH → $MAIN_BRANCH"

# Cleanup local release branch
git checkout "$DEVELOP_BRANCH"
git branch -D "$RELEASE_BRANCH"
echo "🧹 Deleted local branch: $RELEASE_BRANCH"

# -----------------------
# 🛠 Step 2: Create backport branch and PR to develop
# -----------------------
BACKPORT_BRANCH="release/backport-$NEXT_VERSION"

git branch -D "$BACKPORT_BRANCH" 2>/dev/null || true
git checkout -b "$BACKPORT_BRANCH" "$MAIN_BRANCH"

# Set next snapshot version
sed -i '' "s/^version *= *\".*\"/version = \"$NEXT_VERSION\"/" "$VERSION_FILE"
git commit -am "🔄 Prepare for next development version: $NEXT_VERSION"
git push -u origin "$BACKPORT_BRANCH"

gh pr create --base "$DEVELOP_BRANCH" --head "$BACKPORT_BRANCH" \
  --title "🔄 Bump version to $NEXT_VERSION" \
  --body "Backport next snapshot version after $RELEASE_VERSION"

echo "✅ Backport PR created: $BACKPORT_BRANCH → $DEVELOP_BRANCH"

# Cleanup local backport branch
git checkout "$DEVELOP_BRANCH"
git branch -D "$BACKPORT_BRANCH"
echo "🧹 Deleted local branch: $BACKPORT_BRANCH"

echo "🎉 All done!"
