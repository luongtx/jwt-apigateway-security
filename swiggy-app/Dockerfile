FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY swiggy-app/pom.xml ./pom.xml
RUN mvn -q -B dependency:go-offline

COPY swiggy-app/src ./src
RUN mvn -q -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/swiggy-app-0.0.1-SNAPSHOT.jar swiggy-app.jar
COPY docker/java-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8081
ENV APP_JAR=/app/swiggy-app.jar
ENTRYPOINT ["/docker-entrypoint.sh"]

