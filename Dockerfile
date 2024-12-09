# Stage 1: Build the JAR file
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Package Stage - jre -> to make final image smaller
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar /app/runner.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]