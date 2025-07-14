#!/bin/bash
# NEED MORE WORK HERE  FOR NATIVE BUILD TO WORK PROPERLY
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
echo "Building native image for macOS..."
./gradlew clean build nativeCompile


# Build native images for Linux using Docker
# Current project docker mounted directory does not have access to docker so either provide access to it or copy
# project to tmp directory and run docker from there
#echo "Building native image for Linux using Docker..."
#docker run --rm \
#  -v "$PWD":/project \
#  -w /project \
#  --entrypoint /bin/sh \
#  ghcr.io/graalvm/native-image-community:24 \
#  -c "./gradlew nativeCompile"




