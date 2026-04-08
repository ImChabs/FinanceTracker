#!/usr/bin/env sh
set -eu

. "$(cd "$(dirname "$0")" && pwd)/common.sh"

ensure_repo_root

current=$(current_branch || true)
if [ -n "$current" ] && is_protected_branch "$current" && [ "${GIT_ALLOW_PROTECTED_PUSH:-0}" != "1" ]; then
  printf 'Direct pushes to protected branch %s are blocked. Open a PR instead.\n' "$protected_branch" >&2
  exit 1
fi

changed_paths_file=$(mktemp)
trap 'rm -f "$changed_paths_file"' EXIT HUP INT TERM

while IFS=' ' read -r local_ref local_sha remote_ref remote_sha; do
  [ -n "$local_ref" ] || continue

  if [ "$local_sha" = "0000000000000000000000000000000000000000" ]; then
    continue
  fi

  remote_branch=${remote_ref#refs/heads/}
  if is_protected_branch "$remote_branch" && [ "${GIT_ALLOW_PROTECTED_PUSH:-0}" != "1" ]; then
    printf 'Push target %s is protected. Open a PR instead.\n' "$remote_branch" >&2
    exit 1
  fi

  if [ "$remote_sha" = "0000000000000000000000000000000000000000" ]; then
    git diff-tree --no-commit-id --name-only -r "$local_sha" >>"$changed_paths_file"
  else
    git diff --name-only "$remote_sha" "$local_sha" >>"$changed_paths_file"
  fi
done

if [ ! -s "$changed_paths_file" ]; then
  print_header "No push delta detected"
  exit 0
fi

sort -u "$changed_paths_file" -o "$changed_paths_file"

needs_branch_protection=0
needs_compile=0
needs_android_test_compile=0
needs_unit_tests=0
docs_only=1

while IFS= read -r path; do
  [ -n "$path" ] || continue

  if ! is_docs_only_path "$path"; then
    docs_only=0
  fi

  if [ "$path" = ".github/workflows/android-ci.yml" ] || [ "$path" = "docs/branch-protection-required-checks.md" ]; then
    needs_branch_protection=1
  fi

  if is_android_test_only_path "$path"; then
    needs_android_test_compile=1
  fi

  if needs_compile_for_path "$path"; then
    needs_compile=1
  fi

  if needs_unit_tests_for_path "$path"; then
    needs_unit_tests=1
  fi
done <"$changed_paths_file"

if [ "$needs_branch_protection" -eq 1 ]; then
  run_branch_protection_check
fi

if [ "$docs_only" -eq 1 ] && [ "$needs_branch_protection" -eq 0 ]; then
  print_header "Docs-only push detected; skipping Gradle validation"
  exit 0
fi

if [ "$needs_compile" -eq 1 ]; then
  run_compile_check
elif [ "$needs_android_test_compile" -eq 1 ]; then
  run_compile_check ":app:compileDebugAndroidTestKotlin"
fi

if [ "$needs_unit_tests" -eq 1 ]; then
  run_unit_test_check
fi

print_header "Pre-push checks passed"
