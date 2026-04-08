#!/usr/bin/env sh
set -eu

. "$(cd "$(dirname "$0")" && pwd)/common.sh"

ensure_repo_root

print_header "Running pre-commit checks"
git diff --cached --check

staged_files=$(git diff --cached --name-only --diff-filter=ACMR)

if [ -z "$staged_files" ]; then
  print_header "No staged changes to validate"
  exit 0
fi

printf '%s\n' "$staged_files" | while IFS= read -r path; do
  [ -n "$path" ] || continue

  case "$path" in
    local.properties|automation-logs/*|.gradle/*|.gradle-local/*|.kotlin/*|*/build/*|build/*)
      printf 'Blocked staged path: %s\n' "$path" >&2
      exit 1
      ;;
    .idea/inspectionProfiles/Project_Default.xml)
      printf 'Blocked staged IDE path: %s\n' "$path" >&2
      exit 1
      ;;
  esac
done

if git diff --cached --name-only -- handoff/next-block.md | grep -q .; then
  new_history_files=$(git diff --cached --name-only --diff-filter=A -- handoff-history)
  history_count=$(printf '%s\n' "$new_history_files" | sed '/^$/d' | wc -l | tr -d ' ')

  if [ "$history_count" -ne 1 ]; then
    printf 'Staging handoff/next-block.md requires exactly one new handoff-history file.\n' >&2
    exit 1
  fi

  new_history_file=$(printf '%s\n' "$new_history_files" | sed '/^$/d')
  new_prefix=$(printf '%s\n' "$new_history_file" | sed -n 's#^handoff-history/\([0-9][0-9][0-9]\)-.*#\1#p')

  if [ -z "$new_prefix" ]; then
    printf 'New handoff-history file must use a zero-padded numeric prefix: %s\n' "$new_history_file" >&2
    exit 1
  fi

  highest_prefix=$(git ls-tree -r --name-only HEAD -- handoff-history |
    sed -n 's#^handoff-history/\([0-9][0-9][0-9]\)-.*#\1#p' |
    sort |
    tail -n 1)
  [ -n "$highest_prefix" ] || highest_prefix=000
  expected_prefix=$(printf '%03d' $((10#$highest_prefix + 1)))

  if [ "$new_prefix" != "$expected_prefix" ]; then
    printf 'Expected new handoff-history prefix %s but found %s.\n' "$expected_prefix" "$new_prefix" >&2
    exit 1
  fi
fi

if git diff --cached --name-only -- .github/workflows/android-ci.yml docs/branch-protection-required-checks.md | grep -q .; then
  run_branch_protection_check
fi

print_header "Pre-commit checks passed"
