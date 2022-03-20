# Название проекта

Дипломная работа профессии Тестировщик

## Начало работы

1. Открыть интегрированную среду разработки программного обеспечения;
2. Для получения копии Git-репозитория необходимо использовать команду `git clone`;

### Prerequisites

Что нужно установить на ПК для использования (например: Git, браузер и т.д.).
```
1. Интегрированную среду разработки программного обеспечения;
2. Браузер;
3. Git;
4. Docker;
```

### Установка и запуск

Пошаговый процесс установки и запуска

1. Запустить контейнер командой `docker-compose up`;
2. Для запуска сервиса с указанием пути к базе данных mysql использовать команду:
`java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar`,
к postgresql:
`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`;
3. Запустить тесты для mysql:
`./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`, для postgresql
`./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`;
4. Запустить отчет `./gradlew allureReport`.
