# Техническое описание проекта веб-приложения по бронированию комнат

## Документация по API

### Получить бронирования в диапазоне времени

#### Запрос

```http
GET /api/bookings
```

**Параметры:**
- `startTime` (обязательно): Дата-время начала диапазона.
- `endTime` (обязательно): Дата-время конца диапазона.

#### Пример

```http
GET /api/bookings?startTime=2023-01-01T00:00:00&endTime=2023-01-10T00:00:00
```

#### Ответ

```json
[
  {
    "roomName": "Переговорка 1",
    "bookings": [
      {
        "id": 1,
        "startTime": "2023-01-05T10:00:00",
        "endTime": "2023-01-05T12:00:00",
        "bookingPurpose": "Совещание"
      },
      {
        "id": 2,
        "startTime": "2023-01-08T14:00:00",
        "endTime": "2023-01-08T16:00:00",
        "bookingPurpose": "Презентация"
      }
    ]
  },
  {
    "roomName": "Конференц-зал A",
    "bookings": [
      {
        "id": 3,
        "startTime": "2023-01-07T09:00:00",
        "endTime": "2023-01-07T11:00:00",
        "bookingPurpose": "Обсуждение проекта"
      }
    ]
  }
]
```

### Получить бронирования по комнате в диапазоне времени

#### Запрос

```http
GET /api/bookings/room/{roomId}
```

**Параметры:**
- `startTime` (обязательно): Дата-время начала диапазона.
- `endTime` (обязательно): Дата-время конца диапазона.

#### Пример

```http
GET /api/bookings/room/1?startTime=2023-01-01T00:00:00&endTime=2023-01-10T00:00:00
```

#### Ответ

```json
[
  {
    "id": 1,
    "startTime": "2023-01-05T10:00:00",
    "endTime": "2023-01-05T12:00:00",
    "bookingPurpose": "Совещание"
  },
  {
    "id": 2,
    "startTime": "2023-01-08T14:00:00",
    "endTime": "2023-01-08T16:00:00",
    "bookingPurpose": "Презентация"
  }
]
```

### Получить бронирования по пользователю в диапазоне времени

#### Запрос

```http
GET /api/bookings/user/{userId}
```

**Параметры:**
- `startTime` (обязательно): Дата-время начала диапазона.
- `endTime` (обязательно): Дата-время конца диапазона.

#### Пример

```http
GET /api/bookings/user/2?startTime=2023-01-01T00:00:00&endTime=2023-01-10T00:00:00
```

#### Ответ

```json
[
  {
    "id": 1,
    "startTime": "2023-01-05T10:00:00",
    "endTime": "2023-01-05T12:00:00",
    "bookingPurpose": "Совещание"
  },
  {
    "id": 3,
    "startTime": "2023-01-07T09:00:00",
    "endTime": "2023-01-07T11:00:00",
    "bookingPurpose": "Обсуждение проекта"
  }
]
```

### Обновить бронирование

#### Запрос

```http
PUT /api/bookings/{bookingId}
```

**Параметры:**
- `isPeriodic` (опционально): Логический флаг, указывающий, нужно ли обновить всю цепочку бронирования.

#### Пример

```http
PUT /api/bookings/3
```

**Тело запроса (JSON):**
```json
{
  "bookingPurpose": "Новая цель бронирования",
  "isPeriodic": false
}
```

#### Ответ

```json
{
  "id": 3,
  "startTime": "2023-01-07T09:00:00",
  "endTime": "2023-01-07T11:00:00",
  "bookingPurpose": "Новая цель бронирования"
}
```

### Удалить бронирование

#### Запрос

```http
DELETE /api/bookings/{bookingId}
```

**Параметры:**
- `isPeriodic` (опционально): Логический флаг, указывающий, нужно ли удалить всю цепочку бронирования.

#### Пример

```http
DELETE /api/bookings/4?isPeriodic=true
```

#### Ответ

```text
Бронирование успешно удалено
```

### Создать бронирование

#### Запрос

```http
POST /api/bookings
```

**Тело запроса (JSON):**
```json
{
  "roomId": 1,
  "userId": 2,
  "startTime": "2023-02-01T10:00:00",
  "endTime": "2023-02-01T12:00:00",
  "bookingPurpose": "Новое бронирование",
  "isPeriodic": true
}
```

#### Ответ

```json
{
  "id": 5,
  "startTime": "2023-02-01T10:00:00",
  "endTime": "2023-02-01T12:00:00",
  "bookingPurpose": "Новое бронирование"
}
```

## Описание схемы базы данных для бронирования комнат (НЕ АКТУАЛЬНО. В БРОНИРОВАНИИ ПОМЕНЯЛОСЬ ПОЛЕ КОНЦА ПОВТОРЯЮЩХСЯ БРОНИРОВАНИЙ)

### Таблица `Users` (Пользователи)

#### Структура таблицы:

```sql
CREATE TABLE IF NOT EXISTS Users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Описание столбцов:

- `user_id`: Идентификатор пользователя (первичный ключ).
- `username`: Имя пользователя.
- `phone_number`: Номер телефона пользователя (уникальный).
- `fullname`: Полное имя пользователя.
- `password_hash`: Хеш пароля пользователя.
- `role`: Роль пользователя (например, начальник, администратор, техник, преподаватель).
- `created_at`: Время создания пользователя (по умолчанию текущее время).

### Таблица `Rooms` (Комнаты)

#### Структура таблицы:

```sql
CREATE TABLE IF NOT EXISTS Rooms (
    room_id SERIAL PRIMARY KEY,
    room_name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    has_computers BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Описание столбцов:

- `room_id`: Идентификатор комнаты (первичный ключ).
- `room_name`: Название комнаты.
- `capacity`: Вместимость комнаты.
- `has_computers`: Наличие компьютеров в комнате.
- `created_at`: Время создания комнаты (по умолчанию текущее время).

### Таблица `Bookings` (Бронирования)

#### Структура таблицы:

```sql
CREATE TABLE IF NOT EXISTS Bookings (
    booking_id SERIAL PRIMARY KEY,
    room_id INT NOT NULL,
    user_id INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    recurring_interval INT,
    recurring_unit VARCHAR(10),
    recurring_count INT,
    booking_purpose VARCHAR(255),
    CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES Rooms(room_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CHECK (start_time < end_time),
    CHECK (
        (recurring_interval IS NOT NULL AND recurring_unit IS NOT NULL AND recurring_count IS NOT NULL) OR
        (recurring_interval IS NULL AND recurring_unit IS NULL AND recurring_count IS NULL)
    ),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Описание столбцов:

- `booking_id`: Идентификатор бронирования (первичный ключ).
- `room_id`: Идентификатор комнаты (внешний ключ).
- `user_id`: Идентификатор пользователя (внешний ключ).
- `start_time`: Время начала бронирования.
- `end_time`: Время окончания бронирования.
- `recurring_interval`: Интервал повторения периодических бронирований.
- `recurring_unit`: Единица измерения интервала (например, "weeks" для недель).
- `recurring_count`: Количество повторений периодических бронирований.
- `booking_purpose`: Цель бронирования.
- `created_at`: Время создания бронирования (по умолчанию текущее время).

#### Ограничения и проверки:

- `fk_room`: Внешний ключ, связанный с таблицей `Rooms`.
- `fk_user`: Внешний ключ, связанный с таблицей `Users`.
- `CHECK (start_time < end_time)`: Проверка корректности времени начала и окончания бронирования.
- `CHECK (...)`: Проверка наличия значений для интервала, единицы и количества в периодических бронированиях.