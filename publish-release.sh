#!/bin/bash
# This script publishes the release to github
# @Todo Automate this later with a GitHub Action
# Note: Always update version here and changenotes before running this script
set -e

git checkout master
git pull origin master
./gradlew clean build
gh release create v1.0.0 ./build/libs/cli-todoer-*-all.jar --title "v1.0.0" --notes "Initial release of CLI Todoer application with basic functionality."
