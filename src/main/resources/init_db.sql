create database RoomBooking;

create table tag
(
    id serial primary key,
    full_name text,
    short_name text,
    color text
);

create table user_info
(
    id serial primary key,
    username text,
    phone_number text,
    password text,
    is_account_locked boolean,
    role integer
);

create table users
(
    user_id serial primary key,
    fullname text,
    info integer references user_info(id)
);

create table Rooms
(
    room_id serial primary key,
    room_name text,
    capacity integer,
    has_computers boolean,
    has_projector boolean,
    is_cathedral boolean
);

create table recurringexception
(
    id serial primary key,
    date timestamp
);



create table recurringrule
(
    id serial primary key,
    interval integer,
    unit text,
    count integer,
    end_time timestamp
);

create table rule_exception
(
    rule_id integer references recurringrule(id),
    exception_id integer references recurringexception(id)
);

create table groups
(
    id serial primary key,
    name text,
    size integer
);

create table equipment
(
    id serial primary key,
    type integer,
    inv_num bigserial
);


create table bookings
(
    id serial primary key,
    bookingGroupId integer,
    room_id integer references Rooms(room_id),
    owner_id integer references users(user_id),
    start_time timestamp,
    end_time timestamp,
    title text,
    descrition text,
    recurring_rule integer references recurringrule(id),
    status integer
);

create table bookings_staff
(
    booking_id integer references bookings(id),
    staff_id integer references users(user_id)
);

create table bookings_groups
(
    booking_id integer references bookings(id),
    groups_id integer references groups(id)
);

create table bookings_tag
(
    booking_id integer references bookings(id),
    tag_id integer references tag(id)
);

create table bookings_equipment
(
    booking_id integer references bookings(id),
    equipment_id integer references equipment(id)
);



--Вставка тегов
INSERT INTO tag (color, full_name, short_name)
VALUES ('#bde0fe', 'Лекция', 'ЛК'),
       ('#ff8fab', 'Практика', 'ПЗ'),
       ('#fcf6bd', 'Экзамен', 'ЭКЗ'),
       ('#ded6d1', 'Совещание', 'СВ'),
       ('#ff91f2', 'Встреча', 'ВС');




-- Вставка тестовых данных для таблицы Rooms
INSERT INTO Rooms (room_name, capacity, has_computers, has_projector, is_cathedral)
VALUES ('It-5', 40, true, true, true),
        ('It-9', 7, true, true, true),
        ('It-10', 8, true, true, true),
        ('It-11', 25, true, true, true),
        ('It-15', 25, true, true, true),
        ('It-16', 40, true, true, true),
        ('It-17', 40, true, true, true);


insert into equipment (type, inv_num)
values (0, 845216),
       (0, 845217),
       (0, 845218),
       (0, 845219),
       (0, 845220),
       (0, 845221),
       (0, 845222),
       (0, 845223),
       (0, 845224),
       (0, 845225),
       (0, 845226),
       (0, 845227),
       (0, 845228),
       (0, 845229),
       (0, 845230),
       (0, 845231),
       (0, 845232),
       (0, 845233),
       (0, 845234),
       (0, 845235),
       (1, 845236),
       (1, 845237),
       (1, 845238),
       (1, 845239),
       (1, 845240),
       (1, 845241),
       (2, 845242),
       (2, 845243);

insert into groups (name, size)
values ('М8О-401Б-20', 20),
('М8О-402Б-20', 16),
('М8О-403Б-20', 22),
('М8О-404Б-20', 21),
('М8О-405Б-20', 20),
('М8О-406Б-20', 17),
('М8О-407Б-20', 19),
('М8О-408Б-20', 16),
('М8О-409Б-20', 21),
('М8О-410Б-20', 18),
('М8О-411Б-20', 22);


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
