#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

readonly PROMPT='Use the existing repository skill: implement block and produce handoff. Continue from the current handoff/next-block.md, implement exactly one coherent block and verify the smallest meaningful affected scope.'

usage() {
  printf 'Usage: ./run-blocks.sh [--save-logs] <block-count>\n' >&2
}

json_escape() {
  python -c 'import json, sys; print(json.dumps(sys.argv[1]))' "$1"
}

archive_file_count() {
  local archive_dir=$1

  if [[ ! -d "$archive_dir" ]]; then
    printf '0\n'
    return 0
  fi

  find "$archive_dir" -maxdepth 1 -type f | wc -l | tr -d '[:space:]'
}

highest_archive_prefix() {
  local archive_dir=$1
  local highest_prefix=0
  local archive_name=""

  if [[ ! -d "$archive_dir" ]]; then
    printf '0\n'
    return 0
  fi

  while IFS= read -r archive_name; do
    if [[ "$archive_name" =~ ^([0-9]{3})- ]]; then
      local prefix_value=$((10#${BASH_REMATCH[1]}))
      if (( prefix_value > highest_prefix )); then
        highest_prefix=$prefix_value
      fi
    fi
  done < <(find "$archive_dir" -maxdepth 1 -type f -printf '%f\n')

  printf '%s\n' "$highest_prefix"
}

validate_archive_prefix_uniqueness() {
  local archive_dir=$1
  local duplicate_prefixes=""

  if [[ ! -d "$archive_dir" ]]; then
    return 0
  fi

  duplicate_prefixes=$(
    find "$archive_dir" -maxdepth 1 -type f -printf '%f\n' |
      sed -n 's/^\([0-9][0-9][0-9]\)-.*/\1/p' |
      sort |
      uniq -d
  )

  if [[ -n "$duplicate_prefixes" ]]; then
    printf 'Stopping safely: duplicate handoff-history archive prefixes detected: %s\n' \
      "$(printf '%s' "$duplicate_prefixes" | paste -sd ', ' -)" >&2
    return 1
  fi

  return 0
}

validate_archive_progress() {
  local archive_dir=$1
  local previous_archive_count=$2
  local previous_highest_prefix=$3
  local current_archive_count=0
  local current_highest_prefix=0

  validate_archive_prefix_uniqueness "$archive_dir" || return 1

  current_archive_count=$(archive_file_count "$archive_dir")
  current_highest_prefix=$(highest_archive_prefix "$archive_dir")

  if (( current_archive_count != previous_archive_count + 1 )); then
    printf 'Stopping safely: expected exactly one new handoff-history archive file, but file count changed from %s to %s.\n' \
      "$previous_archive_count" \
      "$current_archive_count" >&2
    return 1
  fi

  if (( current_highest_prefix != previous_highest_prefix + 1 )); then
    printf 'Stopping safely: expected the next handoff-history archive prefix to advance from %03d to %03d, but found %03d.\n' \
      "$previous_highest_prefix" \
      "$((previous_highest_prefix + 1))" \
      "$current_highest_prefix" >&2
    return 1
  fi

  return 0
}

format_elapsed() {
  local total_seconds=$1
  local hours=$((total_seconds / 3600))
  local minutes=$(((total_seconds % 3600) / 60))
  local seconds=$((total_seconds % 60))

  printf '%02d:%02d:%02d' "$hours" "$minutes" "$seconds"
}

print_live_status() {
  local spinner_frame=$1
  local block_index=$2
  local block_count=$3
  local detected_reasoning_effort=$4
  local elapsed_seconds=$5

  printf '\r[%s] Block %s/%s | elapsed %s | effort %s' \
    "$spinner_frame" \
    "$block_index" \
    "$block_count" \
    "$(format_elapsed "$elapsed_seconds")" \
    "$detected_reasoning_effort"
}

clear_live_status_line() {
  printf '\r\033[2K'
}

run_codex_with_live_status() {
  local repo_root=$1
  local reasoning_config_override=$2
  local save_logs=$3
  local last_message_file=$4
  local log_file=$5
  local block_index=$6
  local block_count=$7
  local detected_reasoning_effort=$8
  local codex_pid=0
  local codex_exit_code=0
  local start_epoch
  start_epoch=$(date +%s)
  local -a spinner_frames=('/' '-' '\' '|')
  local spinner_index=0

  if [[ "$save_logs" == "true" ]]; then
    codex exec \
      --json \
      --color never \
      --output-last-message "$last_message_file" \
      --cd "$repo_root" \
      --sandbox workspace-write \
      -c "$reasoning_config_override" \
      "$PROMPT" >"$log_file" &
  else
    codex exec \
      --color never \
      --cd "$repo_root" \
      --sandbox workspace-write \
      -c "$reasoning_config_override" \
      "$PROMPT" >/dev/null 2>&1 &
  fi
  codex_pid=$!

  while kill -0 "$codex_pid" 2>/dev/null; do
    local current_epoch
    current_epoch=$(date +%s)
    local elapsed_seconds=$((current_epoch - start_epoch))

    print_live_status \
      "${spinner_frames[$spinner_index]}" \
      "$block_index" \
      "$block_count" \
      "$detected_reasoning_effort" \
      "$elapsed_seconds"

    spinner_index=$(((spinner_index + 1) % ${#spinner_frames[@]}))
    sleep 1
  done

  if wait "$codex_pid"; then
    codex_exit_code=0
  else
    codex_exit_code=$?
  fi

  clear_live_status_line
  return "$codex_exit_code"
}

print_block_result() {
  local block_index=$1
  local block_count=$2
  local result=$3
  local exit_code=$4
  local duration_seconds=$5
  local detected_reasoning_effort=$6
  local token_usage=${7:-}

  if [[ "$result" == "success" ]]; then
    printf '[ok] Block %s/%s | success | duration %s | effort %s' \
      "$block_index" \
      "$block_count" \
      "$(format_elapsed "$duration_seconds")" \
      "$detected_reasoning_effort"
    if [[ -n "$token_usage" ]]; then
      printf ' | tokens %s' "$token_usage"
    fi
    printf '\n'
  else
    printf '[x] Block %s/%s | fail | duration %s | effort %s | error code %s\n' \
      "$block_index" \
      "$block_count" \
      "$(format_elapsed "$duration_seconds")" \
      "$detected_reasoning_effort" \
      "$exit_code"
  fi
}

write_block_manifest() {
  local block_manifest_file=$1
  local block_index=$2
  local detected_reasoning_effort=$3
  local reasoning_config_override=$4
  local result=$5
  local exit_code=$6
  local log_file=$7
  local last_message_file=$8
  local timestamp_utc=$9
  local escaped_timestamp_utc
  local escaped_detected_reasoning_effort
  local escaped_reasoning_config_override
  local escaped_result
  local escaped_log_file
  local escaped_last_message_file

  escaped_timestamp_utc=$(json_escape "$timestamp_utc")
  escaped_detected_reasoning_effort=$(json_escape "$detected_reasoning_effort")
  escaped_reasoning_config_override=$(json_escape "$reasoning_config_override")
  escaped_result=$(json_escape "$result")
  escaped_log_file=$(json_escape "$log_file")
  escaped_last_message_file=$(json_escape "$last_message_file")

  printf '{\n' >"$block_manifest_file"
  printf '  "block_number": %s,\n' "$block_index" >>"$block_manifest_file"
  printf '  "timestamp_utc": %s,\n' "$escaped_timestamp_utc" >>"$block_manifest_file"
  printf '  "detected_reasoning_effort": %s,\n' "$escaped_detected_reasoning_effort" >>"$block_manifest_file"
  printf '  "codex_config_override_requested": %s,\n' "$escaped_reasoning_config_override" >>"$block_manifest_file"
  printf '  "result": %s,\n' "$escaped_result" >>"$block_manifest_file"
  printf '  "exit_code": %s,\n' "$exit_code" >>"$block_manifest_file"
  printf '  "jsonl_log_path": %s,\n' "$escaped_log_file" >>"$block_manifest_file"
  printf '  "last_message_path": %s\n' "$escaped_last_message_file" >>"$block_manifest_file"
  printf '}\n' >>"$block_manifest_file"
}

write_run_manifest() {
  local manifest_file=$1
  local run_id=$2
  local requested_block_count=$3
  local completion_status=$4
  shift 4
  local summary_files=("$@")
  local executed_block_count=${#summary_files[@]}
  local index=0
  local escaped_run_id
  local escaped_completion_status

  escaped_run_id=$(json_escape "$run_id")
  escaped_completion_status=$(json_escape "$completion_status")

  printf '{\n' >"$manifest_file"
  printf '  "run_timestamp_utc": %s,\n' "$escaped_run_id" >>"$manifest_file"
  printf '  "requested_block_count": %s,\n' "$requested_block_count" >>"$manifest_file"
  printf '  "executed_block_count": %s,\n' "$executed_block_count" >>"$manifest_file"
  printf '  "completion_status": %s,\n' "$escaped_completion_status" >>"$manifest_file"
  printf '  "summary_artifact_paths": [\n' >>"$manifest_file"

  for ((index = 0; index < executed_block_count; index++)); do
    printf '    %s' "$(json_escape "${summary_files[$index]}")" >>"$manifest_file"
    if (( index + 1 < executed_block_count )); then
      printf ',' >>"$manifest_file"
    fi
    printf '\n' >>"$manifest_file"
  done

  printf '  ]\n' >>"$manifest_file"
  printf '}\n' >>"$manifest_file"
}

detect_reasoning_effort() {
  local handoff_path=$1
  local detected_effort

  detected_effort=$(
    awk -F': ' '/^- Recommended reasoning effort:/ { print $2; exit }' "$handoff_path" |
      tr '[:upper:]' '[:lower:]' |
      tr -d '[:space:]'
  )

  case "$detected_effort" in
    low|medium|high|xhigh)
      printf '%s\n' "$detected_effort"
      ;;
    *)
      printf 'medium\n'
      ;;
  esac
}

resolve_cli_reasoning_effort() {
  local detected_effort=$1

  case "$detected_effort" in
    xhigh)
      printf 'high\n'
      ;;
    *)
      printf '%s\n' "$detected_effort"
      ;;
  esac
}

print_final_summary() {
  local requested_block_count=$1
  local executed_block_count=$2
  local completion_status=$3
  local run_manifest_file=$4

  printf 'Run summary | requested %s | executed %s | status %s' \
    "$requested_block_count" \
    "$executed_block_count" \
    "$completion_status"
  if [[ -n "$run_manifest_file" ]]; then
    printf ' | run manifest %s' "$run_manifest_file"
  fi
  printf '\n'
}

main() {
  local save_logs="false"

  while [[ $# -gt 0 ]]; do
    case "$1" in
      --save-logs)
        save_logs="true"
        shift
        ;;
      --help|-h)
        usage
        exit 0
        ;;
      -*)
        printf 'Unknown option: %s\n' "$1" >&2
        usage
        exit 1
        ;;
      *)
        break
        ;;
    esac
  done

  if [[ $# -ne 1 ]]; then
    usage
    exit 1
  fi

  local block_count=$1
  if [[ ! "$block_count" =~ ^[1-9][0-9]*$ ]]; then
    printf 'Block count must be a positive integer.\n' >&2
    usage
    exit 1
  fi

  local repo_root
  repo_root=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
  cd "$repo_root"

  local run_id
  run_id=$(date -u +%Y%m%dT%H%M%SZ)
  local run_manifest_file=""
  if [[ "$save_logs" == "true" ]]; then
    run_manifest_file="automation-logs/${run_id}-manifest.json"
  fi
  local -a run_summary_files=()
  local completion_status="stopped_early"
  local executed_block_count=0
  local archive_dir="handoff-history"

  local handoff_path="handoff/next-block.md"
  if [[ ! -f "$handoff_path" ]]; then
    printf 'Stopping safely: %s does not exist.\n' "$handoff_path" >&2
    print_final_summary "$block_count" "$executed_block_count" "$completion_status" ""
    return 1
  fi

  if [[ "$save_logs" == "true" ]]; then
    mkdir -p automation-logs automation-logs/last-messages automation-logs/summaries
  fi

  validate_archive_prefix_uniqueness "$archive_dir" || return 1

  local block_index=0
  for ((block_index = 1; block_index <= block_count; block_index++)); do
    if [[ ! -f "$handoff_path" ]]; then
      printf 'Stopping safely before block %s: %s does not exist.\n' "$block_index" "$handoff_path" >&2
      if [[ "$save_logs" == "true" ]]; then
        write_run_manifest \
          "$run_manifest_file" \
          "$run_id" \
          "$block_count" \
          "$completion_status" \
          "${run_summary_files[@]}"
      fi
      print_final_summary "$block_count" "$executed_block_count" "$completion_status" "$run_manifest_file"
      return 1
    fi

    local log_file="automation-logs/${run_id}-block-${block_index}.jsonl"
    local last_message_file="automation-logs/last-messages/${run_id}-block-${block_index}.md"
    local summary_file="automation-logs/summaries/${run_id}-block-${block_index}.json"
    local detected_reasoning_effort
    detected_reasoning_effort=$(detect_reasoning_effort "$handoff_path")
    local requested_reasoning_effort
    requested_reasoning_effort=$(resolve_cli_reasoning_effort "$detected_reasoning_effort")
    local reasoning_config_override
    printf -v reasoning_config_override 'model_reasoning_effort="%s"' "$requested_reasoning_effort"
    local timestamp_utc
    local block_start_epoch
    block_start_epoch=$(date +%s)
    local block_duration_seconds=0
    local exit_code=0
    local result="success"
    local previous_archive_count=0
    local previous_highest_prefix=0

    previous_archive_count=$(archive_file_count "$archive_dir")
    previous_highest_prefix=$(highest_archive_prefix "$archive_dir")

    if run_codex_with_live_status \
      "$repo_root" \
      "$reasoning_config_override" \
      "$save_logs" \
      "$last_message_file" \
      "$log_file" \
      "$block_index" \
      "$block_count" \
      "$detected_reasoning_effort"
    then
      exit_code=0
    else
      exit_code=$?
      result="failure"
      block_duration_seconds=$(($(date +%s) - block_start_epoch))
      if [[ "$save_logs" == "true" ]]; then
        timestamp_utc=$(date -u +%Y-%m-%dT%H:%M:%SZ)
        write_block_manifest \
          "$summary_file" \
          "$block_index" \
          "$detected_reasoning_effort" \
          "$reasoning_config_override" \
          "$result" \
          "$exit_code" \
          "$log_file" \
          "$last_message_file" \
          "$timestamp_utc"
        run_summary_files+=("$summary_file")
        executed_block_count=${#run_summary_files[@]}
        write_run_manifest \
          "$run_manifest_file" \
          "$run_id" \
          "$block_count" \
          "$completion_status" \
          "${run_summary_files[@]}"
      else
        executed_block_count=$block_index
      fi
      print_block_result \
        "$block_index" \
        "$block_count" \
        "$result" \
        "$exit_code" \
        "$block_duration_seconds" \
        "$detected_reasoning_effort"
      if [[ "$save_logs" == "true" ]]; then
        printf 'Block %s failed. Inspect %s and %s for details.\n' "$block_index" "$log_file" "$last_message_file" >&2
      fi
      print_final_summary "$block_count" "$executed_block_count" "$completion_status" "$run_manifest_file"
      return 1
    fi

    block_duration_seconds=$(($(date +%s) - block_start_epoch))
    if ! validate_archive_progress "$archive_dir" "$previous_archive_count" "$previous_highest_prefix"; then
      result="failure"
      exit_code=1
      if [[ "$save_logs" == "true" ]]; then
        timestamp_utc=$(date -u +%Y-%m-%dT%H:%M:%SZ)
        write_block_manifest \
          "$summary_file" \
          "$block_index" \
          "$detected_reasoning_effort" \
          "$reasoning_config_override" \
          "$result" \
          "$exit_code" \
          "$log_file" \
          "$last_message_file" \
          "$timestamp_utc"
        run_summary_files+=("$summary_file")
        executed_block_count=${#run_summary_files[@]}
        write_run_manifest \
          "$run_manifest_file" \
          "$run_id" \
          "$block_count" \
          "$completion_status" \
          "${run_summary_files[@]}"
      else
        executed_block_count=$block_index
      fi
      print_block_result \
        "$block_index" \
        "$block_count" \
        "$result" \
        "$exit_code" \
        "$block_duration_seconds" \
        "$detected_reasoning_effort"
      print_final_summary "$block_count" "$executed_block_count" "$completion_status" "$run_manifest_file"
      return 1
    fi

    if [[ "$save_logs" == "true" ]]; then
      timestamp_utc=$(date -u +%Y-%m-%dT%H:%M:%SZ)
      write_block_manifest \
        "$summary_file" \
        "$block_index" \
        "$detected_reasoning_effort" \
        "$reasoning_config_override" \
        "$result" \
        "$exit_code" \
        "$log_file" \
        "$last_message_file" \
        "$timestamp_utc"
      run_summary_files+=("$summary_file")
      executed_block_count=${#run_summary_files[@]}
    else
      executed_block_count=$block_index
    fi
    print_block_result \
      "$block_index" \
      "$block_count" \
      "$result" \
      "$exit_code" \
      "$block_duration_seconds" \
      "$detected_reasoning_effort"
  done

  completion_status="completed"
  if [[ "$save_logs" == "true" ]]; then
    write_run_manifest \
      "$run_manifest_file" \
      "$run_id" \
      "$block_count" \
      "$completion_status" \
      "${run_summary_files[@]}"
  fi
  print_final_summary "$block_count" "$executed_block_count" "$completion_status" "$run_manifest_file"
}

if main "$@"; then
  exit 0
else
  exit $?
fi
