#!/bin/bash

readonly HOME=$(cd `dirname $0`; pwd)
readonly SERVER_HOME=${HOME}/server/
readonly WEB_HOME=${HOME}/web/

echo "HOME: ${HOME}"

cd ${SERVER_HOME} && pwd
docker build -t jsh-erp:v1 . --progress=plain --no-cache

cd ${WEB_HOME} && pwd
docker build -t jsh-erp-web:v1 . --progress=plain --no-cache

clear-docker.sh
