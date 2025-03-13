FROM mozilla/sbt:8u292_1.5.4 AS builder
COPY . /app
WORKDIR /app
RUN sbt assembly

FROM openjdk:12
ENV SPARQL_ENDPOINT=http://database:8890/sparql
RUN adduser -d /app cool-uris
RUN  chgrp -R 0 /app && chmod -R g+rwX /app
COPY --from=builder --chown=cool-uris:cool-uris /app/target/scala-2.13/*assembly*.jar /app/cool-uris.jar
USER cool-uris
WORKDIR /app
EXPOSE 8080
CMD ["java","-jar","cool-uris.jar"]