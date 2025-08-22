# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# gradle 파일 복사
COPY gradle gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY gradle.properties .
COPY lint.gradle .

# 소스코드 복사
COPY core-api core-api
COPY core-storage core-storage/
COPY core-support core-support/

# 어플리케이션 빌드
RUN chmod +x ./gradlew
RUN ./gradlew :core-api:bootJar --no-daemon -x test

# 런타임
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy from build
COPY --from=build /app/core-api/build/libs/*.jar app.jar

# Prod Profile
ENV SPRING_PROFILES_ACTIVE=prod

# 8080 Port open
EXPOSE 8080

# jar 시작
ENTRYPOINT ["java", "-jar", "app.jar"]