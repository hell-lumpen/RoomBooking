INSERT INTO rooms (capacity, has_computers, has_projector, room_name)
VALUES
    (10, true, true, 'Пространство самоподготовки IT-0'),
    (15, true, false, 'IT-1'),
    (20, false, true, 'IT-3'),
    (25, true, true, 'Лекторий IT-5'),
    (10, true, true, 'Переговорная IT-10'),
    (10, true, true, 'IT-центр (IT-12)'),
    (30, true, false, 'IT-11'),
    (18, false, true, 'IT-15'),
    (22, true, true, 'Учебная аудитория IT-16'),
    (16, false, false, 'IT-17');

INSERT INTO rrule (frequency, interval, until_date)
VALUES
    ('WEEKLY', 2, '2023-12-06 17:00:00.000'),
    ('MONTHLY', 3, '2023-12-31 23:59:59.999'),
    ('DAILY', 4, '2023-12-31 23:59:59.999');

INSERT INTO users (is_account_locked, user_id, fullname, password, phone_number, role, username)
VALUES
    (false, 1, 'Ненахов Евгений Валентинович', '$2a$10$29c7FqNe945nvsQzTpKuxO.ZaSmCVbGU9vFgQhDpPDjO3Ip5ggVUe', 'username', 'ADMINISTRATOR', 'username'),
    (false, 2, 'Крылов Сергей Сергеевич', '$2a$10$Vub3M8Uqy/6hnQkyzIKz9uzO3AUp6dJVrBPuL2blfJaBYx3hHc.ES', 'username1', 'ADMINISTRATOR', 'username1'),
    (false, 3, 'Булакина Мария Борисовна', '$2a$10$zxX4AZPN3I32t9isvpsYJOs1sGXEIdi3qzjCT0I3SXNUPUb8TjjX.', 'username2', 'ADMINISTRATOR', 'username2'),
    (false, 4, 'Ирбитский Илья Сергеевич', '$2a$10$seChzroHn6eCNk/H7n.3Qu5amP5WsYVz05I6fZs97Kph9rqTS6vcm', 'username3', 'ADMINISTRATOR', 'username3'),
    (false, 5, 'Погосян Михаил Асланович', '$2a$10$DFWuQWujrRXD9fj/PyK3qO3Kmzp97ByXb./qQMCG2JAwmRLtQNK2C', 'username4', 'ADMINISTRATOR', 'username4');


INSERT INTO bookings (periodic_booking_id, r_rule_frequency, r_rule_interval, r_rule_until_date, room_id, user_id, start_time, end_time, booking_purpose)
VALUES
    (null, null, null, null, 5, 3, '2023-10-24 10:00:00.000', '2023-10-24 11:30:00.000', 'Совещание по проведению дня открытых дверей в МАИ'),
    (null, null, null, null, 6, 2, '2023-10-23 20:30:00.000', '2023-10-23 21:00:00.000', 'Организационное собрание в онлайн-магистратуре'),
    (null, null, null, null, 4, 1, '2023-10-23 10:30:00.000', '2023-10-23 12:15:00.000', 'Лекция по предмету "Разработка прикладных компьютерных систем"'),
    (null, null, null, null, 4, 1, '2023-10-23 13:00:00.000', '2023-10-23 16:15:00.000', 'Практические занятия по предмету "Архитектура информационных систем"'),
    (null, null, null, null, 9, 4, '2023-10-23 13:00:00.000', '2023-10-23 16:15:00.000', 'Лабораторные работы по предмету "Основы криптографии"'),
    (null, null, null, null, 4, 5, '2023-10-24 18:00:00.000', '2023-10-24 21:00:00.000', 'Лекция Михаила Аслановича Погосяна'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 'WEEKLY', 2, '2023-12-06 17:00:00.000', 1, 1, '2023-10-25 15:00:00.000', '2023-10-25 17:00:00.000', 'Совещание 1'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 'WEEKLY', 2, '2023-12-06 17:00:00.000', 1, 1, '2023-11-08 15:00:00.000', '2023-11-08 17:00:00.000', 'Совещание 1'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 'WEEKLY', 2, '2023-12-06 17:00:00.000', 1, 1, '2023-11-22 15:00:00.000', '2023-11-22 17:00:00.000', 'Совещание 1'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 'WEEKLY', 2, '2023-12-06 17:00:00.000', 1, 1, '2023-12-06 15:00:00.000', '2023-12-06 17:00:00.000', 'Совещание 1'),
    ('15d0e401-b7ae-46df-bdce-1bb72f214ebd', 'WEEKLY', 2, '2023-12-06 17:00:00.000', 2, 1, '2023-12-06 15:00:00.000', '2023-12-06 17:00:00.000', 'Совещание 2')