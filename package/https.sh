#!/bin/bash

HOST=$1
readonly HOME=$(cd `dirname $0`; pwd)
NAME=$(echo $HOST | sed -e 's/\./_/g')
OUTPUT_DIR=${HOME}/config/nginx/ssl/${NAME}
OUTPUT=${OUTPUT_DIR}/https

mkdir -p ${OUTPUT_DIR}
echo $OUTPUT

openssl req -new -newkey rsa:2048 -sha256 -nodes -out ${OUTPUT}.csr -keyout ${OUTPUT}.key -subj "/C=CN/ST=FuJian/L=FuJian/O=Erp Inc./OU=Web Security/CN=${HOST}"

openssl x509 -req -in ${OUTPUT}.csr -signkey ${OUTPUT}.key -out ${OUTPUT}.crt
