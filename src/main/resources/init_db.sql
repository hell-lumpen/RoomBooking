create database RoomBooking;


CREATE TABLE IF NOT EXISTS RecurringException
(
    -- Идентификатор исключения
    id   SERIAL PRIMARY KEY,

    date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Rule_Exception
(

    rule_id      INTEGER,
    exception_id INTEGER
);



INSERT INTO RecurringException (date)
VALUES ('2024-02-23 12:00:00'),
       ('2024-03-08 12:00:00');


CREATE TABLE IF NOT EXISTS RecurringRule
(
    -- Идентификатор правила
    id           SERIAL PRIMARY KEY,

    -- Интервал повторения для периодических бронирований
    interval     INT,

    -- Единица измерения периода повторения (например, "week" или "month")
    unit         VARCHAR(10),

    -- Количество повторений для периодических бронирований (например для "каждые 2 недели, каждые 5 дней и тп")
    count        INT,

    end_time     TIMESTAMP,

    exception_id INT
);

INSERT INTO RecurringRule (interval, unit, count)
VALUES (8, 'WEEK', 1),
       (4, 'DAY', 1),
       (3, 'DAY', 1),
       (6, 'WEEK', 1);


CREATE TABLE IF NOT EXISTS Users
(
    -- Идентификатор пользователя
    user_id       SERIAL PRIMARY KEY,

    -- Имя пользователя
    username      VARCHAR(50)        NOT NULL,

    -- Номер телефона пользователя (уникальный)
    phone_number  VARCHAR(15) UNIQUE NOT NULL,

    -- Полное имя пользователя
    fullname      VARCHAR(100)       NOT NULL,

    -- Хеш пароля пользователя
    password_hash VARCHAR(255)       NOT NULL,

    -- Роль пользователя
    role          VARCHAR(50)        NOT NULL,

    -- Время создания пользователя (по умолчанию текущее время)
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE IF NOT EXISTS Rooms
(
    -- Идентификатор комнаты
    room_id       SERIAL PRIMARY KEY,

    -- Название комнаты
    room_name     VARCHAR(50) NOT NULL,

    -- Вместимость комнаты
    capacity      INT         NOT NULL,

    -- Наличие компьютеров в комнате
    has_computers BOOLEAN     NOT NULL,

    -- Время создания комнаты (по умолчанию текущее время)
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Bookings
(
    -- Идентификатор бронирования
    booking_id      SERIAL PRIMARY KEY,

    -- Идентификатор комнаты
    room_id         INT       NOT NULL,

    -- Идентификатор пользователя
    user_id         INT       NOT NULL,

    -- Время начала бронирования
    start_time      TIMESTAMP NOT NULL,

    -- Время окончания бронирования
    end_time        TIMESTAMP NOT NULL,

    -- Цель бронирования (просто строка, например "Лекция по базам данных" у группы м8о-410б-20 по расписанию)
    booking_purpose VARCHAR(255),

    recurring_rule  INT,

    -- Внешний ключ для связи с таблицей комнат
    CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES Rooms (room_id),

    -- Внешний ключ для связи с таблицей пользователей
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES Users (user_id),

    -- Условие проверки, что начальное время меньше конечного времени
    CHECK (start_time < end_time),

    -- Условие проверки, что если есть интервал повторения, то указаны и единица, и количество
    CHECK (
        (recurring_interval IS NOT NULL AND recurring_unit IS NOT NULL AND recurring_count IS NOT NULL) OR
        (recurring_interval IS NULL AND recurring_unit IS NULL AND recurring_count IS NULL)
        ),

    -- Время создания бронирования (по умолчанию текущее время)
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Вставка тестовых данных для таблицы Users
INSERT INTO Users (username, phone_number, fullname, password_hash, role)
VALUES ('ivan_ivanov', '1234567890', 'Иван Иванов', 'hashed_password_1', 'начальник'),
       ('anna_petrova', '9876543210', 'Анна Петрова', 'hashed_password_2', 'администратор'),
       ('sergey_smirnov', '5555555555', 'Сергей Смирнов', 'hashed_password_3', 'техник'),
       ('elena_kozlova', '1111111111', 'Елена Козлова', 'hashed_password_4', 'преподаватель'),
       ('dmitry_melnikov', '9999999999', 'Дмитрий Мельников', 'hashed_password_5', 'директор'),
       ('natalia_ivanova', '7777777777', 'Наталья Иванова', 'hashed_password_6', 'начальник'),
       ('alexander_kovalev', '4444444444', 'Александр Ковалев', 'hashed_password_7', 'администратор'),
       ('olga_nikolaeva', '2222222222', 'Ольга Николаева', 'hashed_password_8', 'техник'),
       ('vladimir_petrov', '8888888888', 'Владимир Петров', 'hashed_password_9', 'преподаватель'),
       ('marina_kuznetsova', '6666666666', 'Марина Кузнецова', 'hashed_password_10', 'директор');


-- Вставка тестовых данных для таблицы Rooms
INSERT INTO Rooms (room_name, capacity, has_computers, has_projector)
VALUES ('Комната 101', 10, true, true),
       ('Комната 102', 8, false, true),
       ('Комната 103', 12, true, true),
       ('Комната 201', 6, false, true),
       ('Комната 202', 15, true, true),
       ('Комната 203', 10, true, true),
       ('Комната 301', 20, false, true),
       ('Комната 302', 8, false, true),
       ('Комната 303', 12, true, true),
       ('Комната 401', 10, false, true),
       ('Комната 402', 15, true, true),
       ('Комната 403', 8, false, true),
       ('Комната 501', 25, true, true),
       ('Комната 502', 10, true, true),
       ('Комната 503', 15, true, true);


-- Вставка тестовых данных для таблицы Bookings (одиночные бронирования)
INSERT INTO Bookings (room_id, user_id, start_time, end_time, title)
VALUES (9, 9, '2023-01-09 11:00:00', '2023-01-09 13:00:00', 'Презентация продукта'),
       (10, 10, '2023-01-10 15:00:00', '2023-01-10 17:00:00', 'Техническое обслуживание'),
       (11, 1, '2023-01-11 08:00:00', '2023-01-11 10:00:00', 'Ежедневное совещание'),
       (12, 2, '2023-01-12 14:00:00', '2023-01-12 16:00:00', 'Консультация по проекту'),
       (13, 3, '2023-01-13 10:00:00', '2023-01-13 12:00:00', 'Совещание с партнерами'),
       (14, 4, '2023-01-14 13:00:00', '2023-01-14 15:00:00', 'Бюджетное планирование'),
       (15, 5, '2023-01-15 09:00:00', '2023-01-15 11:00:00', 'Тренинг по лидерству');
-- Добавьте еще одиночные бронирования по аналогии.

-- Вставка тестовых данных для таблицы Bookings (периодические бронирования)
INSERT INTO Bookings (room_id, owner_id, start_time, end_time, recurring_interval, recurring_unit, recurring_count,
                      title)
VALUES (1, 1, '2024-01-29 10:00:00', '2024-01-29 12:00:00', 1, 'WEEK', 4, 'Еженедельный семинар'),
       (1, 1, '2024-01-29 17:00:00', '2024-01-29 18:00:00', 6, 'DAY', 2, 'Ежемесячное обучение'),
       (1, 1, '2024-01-29 09:00:00', '2024-01-29 11:00:00', 1, 'DAY', 1, 'Еженедельный митинг'),
       (1, 1, '2024-01-29 13:00:00', '2024-01-29 15:00:00', 1, 'WEEK', 6, 'Ежеквартальная презентация');
-- Добавьте еще периодические бронирования по аналогии.


-- ##################################################
-- ДОБАВЛЕНИЕ ОГРАНИЧЕНИЙ НА ТАБЛИЦУ Bookings (бронирования)
-- ##################################################

-- Добавление условия проверки, что начальное время меньше конечного времени
ALTER TABLE Bookings
    ADD CHECK (start_time < end_time);
-- ALTER TABLE Bookings ADD CHECK (end_time < recurring_end_time);

-- Добавление условия проверки, что если есть интервал повторения, то указаны и единица, и количество
-- ALTER TABLE Bookings ADD CHECK (
--         (recurring_interval IS NOT NULL AND recurring_unit IS NOT NULL AND recurring_end_time IS NOT NULL) OR
--         (recurring_interval IS NULL AND recurring_unit IS NULL AND recurring_end_time IS NULL)
--     );

-- Добавление поля по умолчанию - текущая метка времени при создании бронирования
ALTER TABLE bookings
    ALTER COLUMN created_at
        SET DEFAULT CURRENT_TIMESTAMP;

-- Создание индекса на столбец room_id в таблице Bookings
CREATE INDEX idx_room_id ON Bookings USING btree (room_id);

-- Получение списка всех бронирований в диапазоне дат

-- SELECT *
-- FROM bookings
-- WHERE
--    -- Для единоразовых событий
--     (start_time BETWEEN '2023-01-11 08:00:00.000000' AND '2023-04-18 11:00:00.000000' AND recurring_end_time IS NULL)
--    OR
--    -- Для повторяющихся событий
--     (start_time BETWEEN '2023-01-11 08:00:00.000000' AND '2023-04-18 11:00:00.000000' AND recurring_end_time IS NOT NULL)
--    OR
--     (recurring_end_time BETWEEN '2023-01-11 08:00:00.000000' AND '2023-04-18 11:00:00.000000');

SELECT *
FROM bookings;

-- Получение списка всех бронирований в диапазоне дат (функция)

CREATE OR REPLACE FUNCTION get_bookings_in_time_range(start_time timestamp, end_time timestamp)
    RETURNS TABLE
            (
                booking_id         bigint,
                room_id            bigint,
                user_id            bigint,
                f_start_time       timestamp,
                f_end_time         timestamp,
                booking_purpose    varchar(255),
                recurring_interval integer,
                recurring_unit     varchar(10),
                recurring_count    integer,
                created_at         timestamp
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT b.booking_id,
               b.room_id,
               b.user_id,
               b.start_time,
               b.end_time,
               b.booking_purpose,
               b.recurring_interval,
               b.recurring_unit,
               b.recurring_count,
               b.created_at
        FROM bookings b
        WHERE (b.start_time BETWEEN start_time AND end_time AND b.recurring_count IS NULL)
           OR (b.recurring_count IS NOT NULL);
END;
$$ LANGUAGE plpgsql;



SELECT b.*
FROM bookings b
WHERE (b.start_time, b.end_time) OVERLAPS ('2023-10-18 10:00:00.000000', '2023-10-18 12:15:00.000000')



INSERT INTO Bookings (periodic_booking_id, room_id, user_id, start_time, end_time, booking_purpose, created_at)
VALUES (1, 1, 1, '2023-10-20T08:00:00', '2023-10-20T10:00:00', 'Meeting', '2023-10-20T07:45:00'),
       (2, 1, 1, '2023-10-20T10:30:00', '2023-10-20T12:30:00', 'Training', '2023-10-20T10:15:00'),
       (3, 1, 1, '2023-10-20T13:00:00', '2023-10-20T15:00:00', 'Conference', '2023-10-20T12:45:00'),
       (4, 1, 1, '2023-10-20T15:30:00', '2023-10-20T17:30:00', 'Workshop', '2023-10-20T15:15:00'),
       (5, 1, 1, '2023-10-21T09:00:00', '2023-10-21T11:00:00', 'Interview', '2023-10-21T08:45:00'),
       (6, 1, 1, '2023-10-21T11:30:00', '2023-10-21T13:30:00', 'Seminar', '2023-10-21T11:15:00'),
       (7, 1, 1, '2023-10-21T14:00:00', '2023-10-21T16:00:00', 'Team Building', '2023-10-21T13:45:00'),
       (8, 1, 1, '2023-10-21T16:30:00', '2023-10-21T18:30:00', 'Product Launch', '2023-10-21T16:15:00'),
       (9, 1, 1, '2023-10-22T10:00:00', '2023-10-22T12:00:00', 'Hackathon', '2023-10-22T09:45:00'),
       (10, 1, 1, '2023-10-22T12:30:00', '2023-10-22T14:30:00', 'Networking Event', '2023-10-22T12:15:00');
