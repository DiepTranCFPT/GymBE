# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Cài đặt thư viện GTK và các dependencies cần thiết cho OpenCV
RUN apt-get update \
    && apt-get install -y libgtk2.0-0 libgl1 libglib2.0-0 libsm6 libxrender1 libxext6 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/new-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/haarcascade_frontalface_default.xml /app/haarcascade_frontalface_default.xml

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]