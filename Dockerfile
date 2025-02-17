# Sử dụng image OpenJDK chính thức làm base image
FROM openjdk:17-jdk-slim

# Copy ứng dụng Spring Boot JAR từ thư mục target vào container
COPY target/your-app.jar app.jar

# Mở cổng 8080 (cổng mặc định của Spring Boot)
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/new-0.0.1-SNAPSHOT.jar"]
