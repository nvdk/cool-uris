FROM mozilla/sbt:8u292_1.5.4 AS builder
COPY . /app
WORKDIR /app
RUN sbt assembly

FROM eclipse-temurin:21-jre-jammy
ENV SPARQL_ENDPOINT=http://database:8890/sparql
COPY --from=builder /app/target/scala-2.13/*assembly*.jar /opt/app/cool-uris.jar
WORKDIR /opt/app
EXPOSE 8080
CMD ["java","-jar","cool-uris.jar"]