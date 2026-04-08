#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

repo_root=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
gradle_wrapper="$repo_root/gradlew"
repo_gradle_home="$repo_root/.gradle-local"
repo_android_home="$repo_root/.android-local"

if [[ ! -f "$gradle_wrapper" ]]; then
  printf 'Could not find gradle wrapper at %s\n' "$gradle_wrapper" >&2
  exit 1
fi

mkdir -p "$repo_gradle_home"
mkdir -p "$repo_android_home"
export GRADLE_USER_HOME="$repo_gradle_home"
export ANDROID_USER_HOME="$repo_android_home"

printf 'Running targeted unit-test validation: :app:testDebugUnitTest\n'

cd "$repo_root"
"$gradle_wrapper" ':app:testDebugUnitTest'
