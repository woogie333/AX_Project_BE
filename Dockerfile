# 1단계: Build stage
FROM amazoncorretto:17-alpine AS build
WORKDIR /app

  # Gradle/Maven 래퍼와 소스 복사
COPY . .
  # 빌드 실행 (테스트 제외로 속도 향상)
RUN ./gradlew clean build -x test

  # 2단계: Run stage
FROM amazoncorretto:17-alpine
WORKDIR /app

  # 빌드 스테이지에서 생성된 jar 파일만 가져오기
COPY --from=build /app/build/libs/*.jar app.jar

  # 실행 권한 부여 및 포트 설정
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]