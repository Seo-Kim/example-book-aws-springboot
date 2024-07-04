#!/usr/bin/env bash
set -eu

ABSPATH=$( readlink -f "$0" )
source "$( dirname "$ABSPATH" )/profile.sh"
source "$( dirname "$ABSPATH" )/switch.sh"

IDLE_PORT=$( find_idle_port )
echo '>> Health Check Start! :: '"${IDLE_PORT}"

for RETRY_COUNT in {1..10}
do
  set +e
  sleep 5
  # 배포 대상 포트로 GET 송신
  RESPONSE=$( curl -s "http://localhost:${IDLE_PORT}/profile" )
  set -e
  if [ -z "${RESPONSE}" ]; then
    if [ "${RETRY_COUNT}" -ge 10 ]; then
      echo '>> Health result: Fail'
      exit 1
    else
      echo 'retry.. :: '"${RETRY_COUNT}"
      continue
    fi
  fi

  echo 'response :: '"${RESPONSE}"
  set +e
  #UP_COUNT=$( echo ${RESPONSE} | grep 'real' | wc -l )
    ## grep | wc -l: grep 결과의 행 수를 반환
  UP_COUNT=$( echo "${RESPONSE}" | grep -c 'real' )
    ## grep -c: 검색 결과의 행 수를 반환
  set -e
  if [ "${UP_COUNT}" -ge 1 ]; then
    echo $'>> Health result: Success\nProxy switching..'
    switch_proxy
    break
  fi

  if [ "${RETRY_COUNT}" -ge 10 ]; then
    echo '>> Health result: Fail'
    exit 1
  fi
done