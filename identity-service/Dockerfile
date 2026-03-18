FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY identity-service/pom.xml ./pom.xml
RUN mvn -q -B dependency:go-offline

COPY identity-service/src ./src
RUN mvn -q -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/identity-service-0.0.1-SNAPSHOT.jar identity-service.jar
COPY docker/java-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 9898
ENV APP_JAR=/app/identity-service.jar
ENTRYPOINT ["/docker-entrypoint.sh"]

