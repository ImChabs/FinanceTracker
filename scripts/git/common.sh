#!/usr/bin/env sh
set -eu

repo_root=$(cd "$(dirname "$0")/../.." && pwd)
protected_branch="${GIT_AUTOMATION_PROTECTED_BRANCH:-main}"

git_repo_root() {
  git rev-parse --show-toplevel
}

ensure_repo_root() {
  if [ "$(git_repo_root)" != "$repo_root" ]; then
    printf 'Run this command from the repository root.\n' >&2
    exit 1
  fi
}

current_branch() {
  git symbolic-ref --quiet --short HEAD
}

is_protected_branch() {
  branch_name=$1
  [ "$branch_name" = "$protected_branch" ]
}

is_docs_only_path() {
  case "$1" in
    .agents/*|.githooks/*|docs/*|handoff/*|handoff-history/*|README.md)
      return 0
      ;;
    scripts/git/*)
      return 0
      ;;
    *)
      return 1
      ;;
  esac
}

is_android_test_only_path() {
  case "$1" in
    app/src/androidTest/*)
      return 0
      ;;
    *)
      return 1
      ;;
  esac
}

needs_compile_for_path() {
  case "$1" in
    app/src/main/*|app/build.gradle.kts|build.gradle.kts|settings.gradle.kts|gradle.properties|gradle/*|gradlew|gradlew.bat)
      return 0
      ;;
    scripts/validate-compile.sh|scripts/validate-compile.ps1|scripts/validate-unit-tests.sh|scripts/validate-unit-tests.ps1)
      return 0
      ;;
    *)
      return 1
      ;;
  esac
}

needs_unit_tests_for_path() {
  case "$1" in
    app/src/test/*|app/src/main/java/*/domain/*|app/src/main/java/*/data/*|app/src/main/java/*ViewModel.kt)
      return 0
      ;;
    *)
      return 1
      ;;
  esac
}

run_branch_protection_check() {
  powershell -ExecutionPolicy Bypass -File "$repo_root/scripts/validate-branch-protection-checks.ps1"
}

run_compile_check() {
  task=${1:-}
  if [ -n "$task" ]; then
    powershell -ExecutionPolicy Bypass -File "$repo_root/scripts/validate-compile.ps1" -GradleTask "$task"
  else
    powershell -ExecutionPolicy Bypass -File "$repo_root/scripts/validate-compile.ps1"
  fi
}

run_unit_test_check() {
  powershell -ExecutionPolicy Bypass -File "$repo_root/scripts/validate-unit-tests.ps1"
}

print_header() {
  printf '[git-automation] %s\n' "$1"
}
