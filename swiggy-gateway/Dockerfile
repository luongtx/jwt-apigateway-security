FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY swiggy-gateway/pom.xml ./pom.xml
RUN mvn -q -B dependency:go-offline

COPY swiggy-gateway/src ./src
RUN mvn -q -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/swiggy-gateway-0.0.1-SNAPSHOT.jar swiggy-gateway.jar
COPY docker/java-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080
ENV APP_JAR=/app/swiggy-gateway.jar
ENTRYPOINT ["/docker-entrypoint.sh"]

