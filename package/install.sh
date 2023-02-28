#!/bin/bash

readonly HOME=$(cd `dirname $0`; pwd)
readonly SERVER_HOME=${HOME}/../jshERP-boot/
readonly WEB_HOME=${HOME}/../jshERP-web/

echo "HOME: ${HOME}"

cd ${SERVER_HOME}
mvn clean && mvn install
# 编译server
mkdir -p ${HOME}/server/
cp ${SERVER_HOME}/target/jshERP-bin.tar.gz ${HOME}/server/jshERP-bin.tar.gz
cp ${HOME}/src/main/docker/Dockerfile
# copy config
mkdir -p ${HOME}/config/erp
cp -R ${SERVER_HOME}/src/main/docker/config/ ${HOME}/config/erp/

cd ${WEB_HOME}
npm run build
cd ${HOME}/web
rm -rf ${HOME}/web/dist/
cp -R ${WEB_HOME}/dist/ ${HOME}/web/dist/
tar cvf jshERP-bin.tar.gz ./dist/
rm -rf ${HOME}/web/dist/


