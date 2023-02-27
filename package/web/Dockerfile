FROM openjdk:8u322-oraclelinux8

ARG artifactId=jsh-erp
# ENV ARTIFACT_ID=${artifactId}
ENV SERVICESTARTFILE=/www/jshERP/start.sh

COPY ./jshERP-bin.tar.gz /www/jshERP.tar.gz
RUN cd /www \
    && tar -xvf jshERP.tar.gz \
    && rm jshERP.tar.gz
CMD cd /www/jshERP && $SERVICESTARTFILE
