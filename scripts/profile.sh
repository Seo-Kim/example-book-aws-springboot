#!/usr/bin/env bash

# 연결되지 않은 프로필 찾기
function find_idle_profile() {
    RESPONSE_CODE=$( curl -s -o /dev/null -w "%(http_code)" 'http://localhost/profile' )
    if [ "${RESPONSE_CODE}" -ge 400 ]; then
      CURRENT_PROFILE=real2
    else
      CURRENT_PROFILE=$( curl -s 'http://localhost/profile' )
    fi

    if [ "${CURRENT_PROFILE}" == 'real1' ]; then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    # bash 함수는 값 반환이 없으므로 함수 사용 위치에서 echo로 값 활용 (반환할 값 외에 echo 있으면 안 됨!)
    echo "${IDLE_PROFILE}"
}

# 연결되지 않은 프로필에 할당된 포트
function find_idle_port() {
    IDLE_PROFILE=$( find_idle_profile )
    if [ "${IDLE_PROFILE}" == 'real1' ]; then
      echo '8081'
    else
      echo '8082'
    fi
}