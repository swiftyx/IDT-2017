#!/usr/bin/env bash

set -euo pipefail

build_dir="build"
output_libs_dir="$build_dir/output/libs"

idt_jar="$build_dir/libs/com.idtus.contest.winter2017.framework.jar"

# -jar is used here since -cp and -jar can't be used together. For details,
# refer to http://stackoverflow.com/a/13018244/431698
java -cp "$output_libs_dir/*:$idt_jar" contest.winter2017.Main "$@"
