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
  echo "Usage: ./release-pr.sh [bump-type]"
  echo ""
  echo "Automates release PR creation:"
  echo "  1. PR from develop to master with snapshot removed"
  echo "  2. Backport PR from master to develop with incremented version"
  echo ""
  echo "Options:"
  echo "  patch     (default)  Increments patch version   e.g. 1.0.0 -> 1.0.1-SNAPSHOT"
  echo "  minor                Increments minor version   e.g. 1.0.0 -> 1.1.0-SNAPSHOT"
  echo "  major                Increments major version   e.g. 1.0.0 -> 2.0.0-SNAPSHOT"
  echo "  -h, --help           Show this help message"
  echo ""
}

if [[ "$1" == "--help" || "$1" == "-h" ]]; then
  show_help
  exit 0
fi

# -----------------------
# 🔁 Version Bump Logic
# -----------------------
BUMP_TYPE="${1:-patch}"

# Ensure clean working directory
if ! git diff --quiet; then
  echo "❌ Uncommitted changes found. Please commit or stash them first."
  exit 1
fi

# Sync branches
echo "🔄 Checking out and updating $DEVELOP_BRANCH..."
git checkout $DEVELOP_BRANCH
git pull origin $DEVELOP_BRANCH
git fetch origin $MAIN_BRANCH

# Get current version
CURRENT_VERSION=$(grep "^version *= *" "$VERSION_FILE" | cut -d'"' -f2)
RELEASE_VERSION="${CURRENT_VERSION/-SNAPSHOT/}"

if [[ "$CURRENT_VERSION" == "$RELEASE_VERSION" ]]; then
  echo "❌ Version is already a release: $RELEASE_VERSION"
  exit 1
fi

echo "🔧 Preparing release for version: $RELEASE_VERSION"

# --- Step 1: Create release branch and PR to master ---
RELEASE_BRANCH="release/$RELEASE_VERSION"
git branch -D "$RELEASE_BRANCH" 2>/dev/null || true
git checkout -b "$RELEASE_BRANCH"

# Remove -SNAPSHOT from version
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

# --- Step 2: Create backport branch and PR to develop ---
IFS='.' read -r MAJOR MINOR PATCH <<< "$RELEASE_VERSION"

case "$BUMP_TYPE" in
  patch)
    PATCH=$((PATCH + 1))
    ;;
  minor)
    MINOR=$((MINOR + 1))
    PATCH=0
    ;;
  major)
    MAJOR=$((MAJOR + 1))
    MINOR=0
    PATCH=0
    ;;
  *)
    echo "❌ Unknown bump type: $BUMP_TYPE"
    show_help
    exit 1
    ;;
esac

NEXT_VERSION="${MAJOR}.${MINOR}.${PATCH}-SNAPSHOT"
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
