FROM mozilla/sbt:8u292_1.5.4 as builder
USER 0
COPY . /app
WORKDIR /app
RUN chown -R 1001:0 /app
USER 1001
RUN sbt assembly

FROM openjdk:12
USER 0
ENV SPARQL_ENDPOINT=http://database:8890/sparql
COPY --from=builder /app/target/scala-2.13/*assembly*.jar /app/cool-uris.jar
RUN chown -R 1001:0 /app
USER 1001
CMD ["java","-jar","/app/cool-uris.jar"]
