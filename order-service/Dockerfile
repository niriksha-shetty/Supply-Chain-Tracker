# Stage 1: Build JAR using Maven Wrapper
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

# Copy Maven wrapper and POM first for dependency layer caching
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B || true

# Copy source code and package
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Minimal Runtime JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
