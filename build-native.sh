#!/bin/bash
# This script builds native builds for macOS, Linux using GraalVM native-image.
# NOTE: Ensure you have GraalVM installed and the native-image tool available in your PATH.
set -e  # Exit immediately if a command exits with a non-zero status.

# Check if GraalVM native-image is installed
echo "Checking for GraalVM native-image tool..."
if java -version 2>&1 | grep -iq graalvm; then
  echo "✅ GraalVM is active"
else
  echo "❌ GraalVM is NOT active"
  exit 1
fi

# Build native images for macOS
#echo "Building native image for macOS..."
#./gradlew clean build nativeCompile


# Build native images for Linux using Docker
echo "Building native image for Linux using Docker..."
docker run --rm \
  -v "$PWD":/project \
  -w /project \
  ghcr.io/graalvm/native-image-community:24 \
  bash -c "./gradlew nativeCompile"




