#!/usr/bin/env bash
set -eu

ABSPATH=$( readlink -f "$0" )
source "$( dirname "$ABSPATH" )/profile.sh"

function switch_proxy() {
    IDLE_PORT=$( find_idle_port )
    echo '>> rewrite url to config file :: '"${IDLE_PORT}"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" \
      | cat > "/etc/nginx/conf.d/service-url.inc"
    #  | sudo tee '/etc/nginx/conf.d/service-url.inc'
        ## tee:
        ## sudo password required

    echo '>> NginX reload'
#    cat ~/app/stdPwd | sudo -S nginx -t
#    cat ~/app/stdPwd | sudo -S nginx -s reload
    nginx -t
    nginx -s reload
      ## sudo: a terminal is required to read the apssword; either use the -S option to read from standard input or configure an askpass helper
      ## sudo: a password is required < ssh -t -s "sudo"
#    echo ~/app/stdPwd | sudo -S nginx -s reload
    #service nginx reload
      ## subsystem request failed on channel 0 < ssh -t -s "sudo"
      ## Failed to relaod nginx.service: Interactive authentication required
    #sudo systemctl reload nginx
      ##
}