#!/usr/bin/env sh
set -eu

repo_root=$(cd "$(dirname "$0")/../.." && pwd)
cd "$repo_root"

git config core.hooksPath .githooks
printf 'Configured git core.hooksPath to .githooks\n'
