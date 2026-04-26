# 1단계: 빌드 (이미 21로 되어 있을 겁니다)
FROM amazoncorretto:21-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# 2단계: 실행 (여기가 중요! 17이 아니라 21이어야 합니다)
FROM amazoncorretto:21-alpine
WORKDIR /app
# 빌드 단계에서 생성된 jar 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
