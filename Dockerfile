FROM openjdk:21-oracle

RUN microdnf install findutils

WORKDIR /app

COPY ./docker-entrypoint.sh /app/docker-entrypoint.sh

ENTRYPOINT ["./docker-entrypoint.sh"]
