FROM nginx:stable-alpine

ENV TZ=Asia/Singapore
COPY ./jshERP-bin.tar.gz /www/jshERP.tar.gz
RUN cd /www \
    && tar -xf jshERP.tar.gz \
    && rm jshERP.tar.gz \
    && ls -alh /www && ls -alh /www/dist/
