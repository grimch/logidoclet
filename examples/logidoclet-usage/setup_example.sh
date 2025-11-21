#!/bin/bash
# This script sets up the example project structure for Maven usage.

# Exit immediately if a command exits with a non-zero status.
set -e

# Define the project root relative to the script location
PROJECT_ROOT=$(dirname "$0")/../..

# The source for the sample code
SAMPLE_SRC="$PROJECT_ROOT/src/test/resources/sample_module"

# The destination for the example project's source
DEST_SRC="src"

echo "Cleaning up previous example source..."
if [ -d "$DEST_SRC" ]; then
    rm -rf "$DEST_SRC"
fi

echo "Creating new source directory structure..."
mkdir -p "$DEST_SRC/main/java"

echo "Copying sample module source..."
# The -r flag copies directories recursively. The /* ensures the contents are copied.
cp -r "$SAMPLE_SRC"/* "$DEST_SRC/main/java/"

echo "Setup complete. The example project is ready in 'examples/maven-usage'."
echo "You can now run 'mvn clean javadoc:javadoc' in this directory."
