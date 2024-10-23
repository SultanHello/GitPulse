FROM openjdk:22-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

COPY target/EurekaGitService-0.0.1-SNAPSHOT.jar app.jar

# Указываем команду для запуска приложения
CMD ["java", "-jar", "app.jar"]