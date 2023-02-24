FROM openjdk:8u322-oraclelinux8

ARG artifactId=jsh-erp
# ENV ARTIFACT_ID=${artifactId}
ENV SERVICESTARTFILE=/www/jshERP/start.sh

COPY ./dist/jshERP-bin.tar.gz /www/
CMD $SERVICESTARTFILE
