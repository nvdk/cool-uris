FROM mozilla/sbt:8u292_1.5.4 as builder
COPY . /app
WORKDIR /app
RUN sbt assembly

FROM openjdk:12
ENV SPARQL_ENDPOINT=http://database:8890/sparql
COPY --from=builder /app/target/scala-2.13/*assembly*.jar /app/cool-uris.jar
CMD ["java","-jar","/app/cool-uris.jar"]