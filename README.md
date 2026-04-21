# Task Time Tracker API

Тестовое задание для позиции Java Backend Developer.

Сервис позволяет:
- создавать задачи;
- получать задачу по ID;
- менять статус задачи;
- создавать записи о затраченном времени по задачам;
- получать время сотрудника за период;
- работать через Bearer JWT;
- смотреть Swagger UI;
- запускать unit-тесты и интеграционный тест DAO-слоя.

## Почему решение устроено именно так

Я старался сделать не просто "чтобы работало", а чтобы решение было читаемым и легко расширялось:

- **Controller** отвечает только за HTTP;
- **Service** содержит бизнес-логику;
- **Mapper (MyBatis)** работает с БД;
- **DTO** отделены от доменных моделей;
- **Validation** проверяет входные данные сразу на входе;
- **Global exception handler** возвращает понятные ошибки;
- **JWT** показывает базовую защиту API;
- **Testcontainers** проверяет работу DAO-слоя на PostgreSQL, а не только "в памяти".

## Стек
- Java 17+
- Spring Boot 3
- MyBatis
- Maven
- H2
- JUnit 5
- Mockito
- SpringDoc OpenAPI (Swagger)
- JWT
- Testcontainers + PostgreSQL

## Структура проекта

```text
src/main/java/.../controller     REST-контроллеры
src/main/java/.../service        бизнес-логика
src/main/java/.../mapper         MyBatis mapper'ы
src/main/java/.../dto            входные/выходные DTO
src/main/java/.../exception      обработка ошибок
src/main/java/.../security       JWT и Spring Security
src/main/resources/db            schema + test data
src/test/java/...                unit и integration tests
postman/                         коллекция запросов
```

## Как запустить

### 1. Сборка
```bash
mvn clean test
mvn spring-boot:run
```

### 2. Приложение будет доступно
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 console: `http://localhost:8080/h2-console`

### 3. Параметры H2
- JDBC URL: `jdbc:h2:mem:trackerdb`
- User: `sa`
- Password: `sa`

## Как получить JWT

### Запрос
`POST /api/auth/token`

Body:
```json
{
  "employeeId": 1001,
  "fullName": "Ivan Ivanov"
}
```

### Ответ
```json
{
  "tokenType": "Bearer",
  "accessToken": "..."
}
```

После этого добавьте заголовок:

```text
Authorization: Bearer <ваш_токен>
```

## Основные эндпойнты

### Создать задачу
`POST /api/tasks`

```json
{
  "title": "Разобрать требования",
  "description": "Прочитать тестовое и выделить сущности"
}
```

### Получить задачу
`GET /api/tasks/{id}`

### Изменить статус задачи
`PATCH /api/tasks/{id}/status`

```json
{
  "status": "IN_PROGRESS"
}
```

### Создать запись времени
`POST /api/time-records`

```json
{
  "employeeId": 1001,
  "taskId": 1,
  "startTime": "2026-04-20T10:00:00",
  "endTime": "2026-04-20T12:30:00",
  "workDescription": "Проработка структуры проекта"
}
```

### Получить время сотрудника за период
`GET /api/time-records?employeeId=1001&from=2026-04-01T00:00:00&to=2026-04-30T23:59:59`

## Важные бизнес-правила

- нельзя создать запись времени для несуществующей задачи;
- `endTime` должен быть позже `startTime`;
- нельзя создавать запись времени для задачи со статусом `DONE`;
- при выдаче отчета дополнительно считается общая длительность в минутах.

## Тесты

### Unit tests
Покрыты:
- `TaskService`
- `TimeRecordService`

### Integration test
- `TimeRecordMapperIntegrationTest`
- запускается через Testcontainers и PostgreSQL

## Postman
В проекте есть коллекция:
`postman/task-time-tracker.postman_collection.json`

## Что можно улучшить дальше
- refresh token;
- роли и права доступа;
- пагинация списка задач;
- запрет пересечения временных интервалов по сотруднику;
- audit/logging;
- Dockerfile и docker-compose.

## Примечание
Для простоты основной runtime сделан на H2, чтобы проект можно было проверить быстрее.  
При этом DAO-слой дополнительно проверяется интеграционным тестом на PostgreSQL через Testcontainers.
