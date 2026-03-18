FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and download dependencies (better Docker cache)
COPY swiggy-service-registry/pom.xml ./pom.xml
RUN mvn -q -B dependency:go-offline

# Copy source and build the application
COPY swiggy-service-registry/src ./src
RUN mvn -q -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/swiggy-service-registry-0.0.1-SNAPSHOT.jar swiggy-service-registry.jar
COPY docker/java-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8761
ENV APP_JAR=/app/swiggy-service-registry.jar
ENTRYPOINT ["/docker-entrypoint.sh"]

