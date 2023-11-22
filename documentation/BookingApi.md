# Документация по контроллеру бронирования

REST-контроллер для управления бронированием комнат.

## Методы

### 1. **Получение информации о бронировании**
- **Конечная точка:** `GET /api/bookings/{id}`
- **Описание:** Получить подробную информацию о конкретном бронировании.
- **Параметры:**
    - `id` (Путь, Long) - Идентификатор бронирования.
- **Возвращает:** `RoomBookingDetailsDTO`
- **Исключения:**
    - `BookingNotFoundException` - При попытке получения несуществующего бронирования.

### 2. **Получение всех бронирований**
- **Конечная точка:** `GET /api/bookings/all`
- **Описание:** Получить все бронирования, хранящиеся в базе данных.
- **Возвращает:** Список `RoomBookingDTO`.

### 3. **Получение бронирований в заданном временном диапазоне**
- **Конечная точка:** `GET /api/bookings`
- **Описание:** Получить все бронирования в указанном временном диапазоне, сгруппированные по комнатам.
- **Параметры:**
    - `startTime` (Параметр запроса, LocalDateTime) - Дата и время начала диапазона.
    - `endTime` (Параметр запроса, LocalDateTime) - Дата и время конца диапазона.
- **Возвращает:** Список `Pair` (название, бронирования).

### 4. **Получение бронирований по комнате в заданном временном диапазоне**
- **Конечная точка:** `GET /api/bookings/room/{roomId}`
- **Описание:** Получить бронирования для определенной комнаты в указанном временном диапазоне.
- **Параметры:**
    - `roomId` (Путь, Long) - Идентификатор комнаты.
    - `startTime` (Параметр запроса, LocalDateTime) - Дата и время начала диапазона.
    - `endTime` (Параметр запроса, LocalDateTime) - Дата и время конца диапазона.
- **Возвращает:** Список `RoomBookingDTO`.

### 5. **Получение бронирований по пользователю в заданном временном диапазоне**
- **Конечная точка:** `GET /api/bookings/user/{userId}`
- **Описание:** Получить бронирования для определенного пользователя в указанном временном диапазоне.
- **Параметры:**
    - `userId` (Путь, Long) - Идентификатор пользователя.
    - `startTime` (Параметр запроса, LocalDateTime) - Дата и время начала диапазона.
    - `endTime` (Параметр запроса, LocalDateTime) - Дата и время конца диапазона.
- **Возвращает:** Список `RoomBookingDTO`.
- **Исключения:**
    - `AccessDeniedException` - Если у пользователя недостаточно прав.

### 6. **Обновление или создание бронирования**
- **Конечная точка:** `PUT /api/bookings`
- **Описание:** Изменить или создать новое бронирование комнаты.
- **Параметры:**
    - `request` (Тело запроса, RoomBookingRequestDTO) - Информация для изменения бронирования.
- **Возвращает:** `RoomBookingDetailsDTO`.
- **Исключения:**
    - `BookingConflictException` - В случае конфликта с существующими бронированиями.
    - `AccessDeniedException` - Если у пользователя недостаточно прав.

### 7. **Удаление бронирования**
- **Конечная точка:** `DELETE /api/bookings/{bookingId}`
- **Описание:** Удалить бронирование.
- **Параметры:**
    - `bookingId` (Путь, Long) - Идентификатор бронирования.
- **Возвращает:** Строка.
- **Исключения:**
    - `BookingNotFoundException` - При попытке удаления несуществующего бронирования.
    - `AccessDeniedException` - Если у пользователя недостаточно прав.

### 8. **Создание бронирования**
- **Конечная точка:** `POST /api/bookings`
- **Описание:** Создать новое бронирование комнаты.
- **Параметры:**
    - `request` (Тело запроса, RoomBookingRequestDTO) - Информация для создания бронирования.
- **Возвращает:** `Booking`.
- **Исключения:**
    - `BookingConflictException` - В случае конфликта с существующими бронированиями.
    - `RoomNotFoundException` - Если комната не найдена по идентификатору.
    - `UserNotFoundException` - Если пользователь не найден по идентификатору.
    - `AccessDeniedException` - Если у пользователя недостаточно прав.

## Примеры

### Пример 1: Получение информации о бронировании
```http
GET /api/bookings/1
```
Ответ:
```json
{
  "id": 1,
  "bookingGroupId": "some-uuid",
  "room": {
    "id": 123,
    "name": "Conference Room",
    "capacity": 50,
    "hasComputers": true,
    "hasProjector": false
  },
  "owner": {
    "id": 1,
    "fullName": "John Doe",
    "phoneNumber": "81234567890",
    "role": "ADMIN",
    "isAccountLocked": false
  },
  "startTime": "2023-01-01T10:00:00",
  "endTime": "2023-01-01T12:00:00",
  "title": "Встреча",
  "description": "Совещание",
  "staff": [
    {
      "id": 1,
      "fullName": "John Doe",
      "phoneNumber": "81234567890",
      "role": "ADMIN",
      "isAccountLocked": false
    },
    {
      "id": 1,
      "fullName": "John Doe",
      "phoneNumber": "81234567890",
      "role": "ADMIN",
      "isAccountLocked": false
    }
  ],
  "groups": [
    {
      "id": 1,
      "name": "M80-410Б-20"
    },
    {
      "id": 2,
      "name": "М30-101Б-23"
    }
  ],
  "tag": {
    "id": 1,
    "fullName": "Sample Tag",
    "shortName": "ST",
    "color": "#1a2b3c"
  }
}
```

### Пример 2: Получение всех бронирований
```http
GET /api/bookings/all
```
Ответ:
```json
[
  {
    "id": 1,
    "bookingGroupId": "some-uuid",
    "room": "Конференц-зал 1",
    "owner": "Джон Доу",
    "startTime": "2023-01-01T10:00:00",
    "endTime": "2023-01-01T12:00:00",
    "title": "Встреча",
    "tag": {
      "id": 1,
      "fullName": "Sample Tag",
      "shortName": "ST",
      "color": "#1a2b3c"
    }
  }
]
```

### Пример 3: Получение бронирований в заданном временном диапазоне
```http
GET /api/bookings?startTime=2023-01-01T09:00:00&endTime=2023-01-01T14:00:00
```
Ответ:
```json
[
  {
    "name": "Конференц-зал 1",
    "bookings": [
      {...},
      ...
    ]
  },
  ...
]
```

### Пример 4: Получение бронирований по комнате в заданном временном диапазоне
```http
GET /api/bookings/room/1?startTime=2023-01-01T09:00:00&endTime=2023-01-01T14:00:00
```
Ответ:
```json
[
  {
    "id": 1,
    "bookingGroupId": "some-uuid",
    "room": "Конференц-зал 1",
    "owner": "Джон Доу",
    "startTime": "2023-01-01T10:00:00",
    "endTime": "2023-01-01T12:00:00",
    "title": "Встреча",
    "tag": {
      "id": 1,
      "fullName": "Sample Tag",
      "shortName": "ST",
      "color": "#1a2b3c"
    }
  }
]
```

### Пример 5: Получение бронирований по пользователю в заданном временном диапазоне
```http
GET /api/bookings/user/1?startTime=2023-01-01T09:00:00&endTime=2023-01-01T14:00:00
```
Ответ:
```json
[
  {
    "id": 1,
    "bookingGroupId": "some-uuid",
    "room": "Конференц-зал 1",
    "owner": "Джон Доу",
    "startTime": "2023-01-01T10:00:00",
    "endTime": "2023-01-01T12:00:00",
    "title": "Встреча",
    "tag": {
      "id": 1,
      "fullName": "Sample Tag",
      "shortName": "ST",
      "color": "#1a2b3c"
    }
  }
]
```

### Пример 6: Обновление или создание бронирования
```http
PUT /api/bookings
```
Тело запроса:
```json
{
  "roomId": 1,
  "ownerId": 1,
  "startTime": "2023-01-01T14:00:00",
  "endTime": "2023-01-01T16:00:00",
  "description": "Обновленное совещание",
  "title": "Обновленная встреча",
  "staffId": [2, 3],
  "groupsId": [1, 2],
  "tag": {
  "id": 1,
  "fullName": "Sample Tag",
  "shortName": "ST",
  "color": "#1a2b3c"
}
}
```
Ответ:
```json
{
  "id": 2,
  "bookingGroupId": "updated-uuid",
  "room":{
    "id": 123,
    "name": "Conference Room",
    "capacity": 50,
    "hasComputers": true,
    "hasProjector": false
  },
  "owner": {
    "id": 1,
    "fullName": "John Doe",
    "phoneNumber": "81234567890",
    "role": "ADMIN",
    "isAccountLocked": false
  },
  "startTime": "2023-01-01T14:00:00",
  "endTime": "2023-01-01T16:00:00",
  "title": "Обновленная встреча",
  "description": "Обновленное совещание",
  "staff": [
    {
      "id": 1,
      "fullName": "John Doe",
      "phoneNumber": "81234567890",
      "role": "ADMIN",
      "isAccountLocked": false
    },
    {
      "id": 1,
      "fullName": "John Doe",
      "phoneNumber": "81234567890",
      "role": "ADMIN",
      "isAccountLocked": false
    }
  ],
  "groups": [
    {
      "id": 1,
      "name": "M80-410Б-20"
    },
    {
      "id": 2,
      "name": "М30-101Б-23"
    }
  ],
  "tag": {
    "id": 1,
    "fullName": "Sample Tag",
    "shortName": "ST",
    "color": "#1a2b3c"
  }
}
```

### Пример 7: Удаление бронирования
```http
DELETE /api/bookings/2
```
Ответ:
```json
"Бронирование успешно удалено"
```

### Пример 8: Создание бронирования
```http
POST /api/bookings
```
Тело запроса:
```json
{
  "roomId": 1,
  "ownerId": 1,
  "startTime": "2023-01-01T16:00:00",
  "endTime": "2023-01-01T18:00:00",
  "description": "Новое совещание",
  "title": "Новая встреча",
  "staffId": [2, 3],
  "groupsId": [1, 2],
  "tag": {
    "id": 1,
    "fullName": "Sample Tag",
    "shortName": "ST",
    "color": "#1a2b3c"
  }
}
```
Ответ:
```json
{
  "id": 3,
  "bookingGroupId": "new-uuid",
  "room": {
    "id": 123,
    "name": "Conference Room",
    "capacity": 50,
    "hasComputers": true,
    "hasProjector": false
  },
  "owner": {
    "id": 1,
    "fullName": "John Doe",
    "phoneNumber": "81234567890",
    "role": "ADMIN",
    "isAccountLocked": false
  },
  "startTime": "2023-01-01T16:00:00",
  "endTime": "2023-01-01T18:00:00",
  "title": "Новая встреча",
  "description": "Новое совещание",
  "staff": [
      {
        "id": 1,
        "fullName": "John Doe",
        "phoneNumber": "81234567890",
        "role": "ADMIN",
        "isAccountLocked": false
      }, {
        "id": 2,
        "fullName": "John Doe",
        "phoneNumber": "81234567890",
        "role": "ADMIN",
        "isAccountLocked": false
      }
  ],
  "groups":
  [
    {
      "id": 1,
      "name": "M80-410Б-20"
    },
    {
      "id": 2,
      "name": "М30-101Б-23"
    }
  ],
  "tag": {
    "id": 1,
    "fullName": "Sample Tag",
    "shortName": "ST",
    "color": "#1a2b3c"
  }
}
```