@echo off
REM This script sets up the example project structure for Maven usage.

REM Define the destination for the example project's source
set "DEST_SRC=src"

echo "Cleaning up previous example source..."
if exist "%DEST_SRC%" (
    rmdir /s /q "%DEST_SRC%"
)

echo "Creating new source directory structure..."
mkdir "%DEST_SRC%\main\java"

echo "Copying sample module source..."
REM The /e switch copies directories and subdirectories, including empty ones.
REM The /i switch assumes the destination is a directory if it doesn't exist.
REM The /h switch copies hidden and system files.
REM The /y switch suppresses prompts to overwrite existing files.
xcopy "..\..\src\test\resources\sample_module" "%DEST_SRC%\main\java" /e /i /h /y

echo "Setup complete. The example project is ready in 'examples/maven-usage'."
echo "You can now run 'mvn javadoc:javadoc' in this directory."

