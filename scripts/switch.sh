#!/usr/bin/env bash
set -eu

ABSPATH=$( readlink -f "$0" )
source "$( dirname "$ABSPATH" )/profile.sh"

function switch_proxy() {
    IDLE_PORT=$( find_idle_port )
    echo '>> rewrite url to config file :: '"${IDLE_PORT}"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" \
      | sudo tee '/etc/nginx/conf.d/service-url.inc'

    echo '>> NginX reload'
    sudo nginx -t
    sudo nginx -s reload
}