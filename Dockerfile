FROM hseeberger/scala-sbt:8u171_2.12.6_1.1.5 as builder
COPY . /app
WORKDIR /app
RUN sbt assembly

FROM openjdk:12
ENV SPARQL_ENDPOINT=http://database:8890/sparql
COPY --from=builder /app/target/scala-2.12/*assembly*.jar /app/cool-uris.jar
CMD ["java","-jar","/app/cool-uris.jar"]