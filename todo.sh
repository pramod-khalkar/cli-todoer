#!/bin/bash
# This script builds the CLI Todoer application and runs it with the provided arguments.
GEN_JAR_FILE=$(ls ./build/libs/cli-todoer-*-all.jar 2>/dev/null | head -n 1)
if [ ! -f "$GEN_JAR_FILE" ]; then
  ./gradlew clean build
  ./gradlew shadowJar
fi
java -cp ./build/libs/cli-todoer-*-all.jar org.clitodoer.TodoLauncher "$@"