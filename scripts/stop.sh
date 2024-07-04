#!/usr/bin/env bash
set -eu

# 현재 실행 중인 파일의 위치
ABSPATH=$( readlink -f "$0" )
  ## $0:
# profile.sh 불러오기 (import / include)
source "$( dirname "$ABSPATH" )/profile.sh"

IDLE_PORT=$( find_idle_port )
echo '>> Find PID on Running :: '"${IDLE_PORT}"
set +e
CURRENT_PID=$( lsof -ti tcp:"${IDLE_PORT}" )
  ## lsof:
  ## -ti:
set -e
if [ -z "${CURRENT_PID}" ]; then
  echo 'Nothing found PID to Stop'
else
  echo 'Stop PID :: '"$CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 10
fi