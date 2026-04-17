#!/usr/bin/env sh
set -e

if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "ERROR: Gradle is not installed. Please install Gradle or regenerate wrapper with 'gradle wrapper'." >&2
  exit 1
fi
