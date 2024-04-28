# Этап 1: Сборка приложения
FROM gradle:8.7.0-jdk AS builder

# Копируем исходный код в контейнер
COPY . /app

# Устанавливаем рабочую директорию
WORKDIR /app

# Собираем приложение
RUN gradle build --no-daemon

# Этап 2: Запуск приложения
FROM openjdk:17-jdk-slim

# Копируем JAR файл из предыдущего этапа
COPY --from=builder /app/build/libs/smartCampus.jar /app/smartCampus.jar
EXPOSE 8000

# Запускаем JAR файл
CMD ["java", "-jar", "/app/smartCampus.jar"]
