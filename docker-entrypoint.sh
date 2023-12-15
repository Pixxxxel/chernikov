#!/bin/sh

set -o errexit
set -o nounset
# shellcheck disable=SC2039
set -o pipefail

# shellcheck disable=SC2121
set JAVA_OPTS=-XX:MaxDirectMemorySize=4g

./gradlew bootRun
