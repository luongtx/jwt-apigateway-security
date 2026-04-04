FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY notification-service/pom.xml ./pom.xml
RUN mvn -q -B dependency:go-offline

COPY notification-service/src ./src
RUN mvn -q -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar
COPY docker/java-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8083
ENV APP_JAR=/app/notification-service.jar
ENTRYPOINT ["/docker-entrypoint.sh"]
