# =============================================
# Stage 1: Build ứng dụng bằng Maven
# =============================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache Maven dependencies (tăng tốc rebuild)
COPY pom.xml .
COPY .mvn .mvn
RUN mvn dependency:go-offline -B -q

# Copy source code và build
COPY src ./src
RUN mvn -q -DskipTests package

# =============================================
# Stage 2: Runtime image tối giản
# =============================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Security: Chạy app bằng non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
