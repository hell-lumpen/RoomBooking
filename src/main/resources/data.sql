INSERT INTO rooms (capacity, has_computers, has_projector, room_name)
VALUES (10, true, true, 'Пространство самоподготовки IT-0'),
    (15, true, false, 'IT-1'),
    (20, false, true, 'IT-3'),
    (25, true, true, 'Лекторий IT-5'),
    (10, true, true, 'Переговорная IT-10'),
    (10, true, true, 'IT-центр (IT-12)'),
    (30, true, false, 'IT-11'),
    (18, false, true, 'IT-15'),
    (22, true, true, 'Учебная аудитория IT-16'),
    (16, false, false, 'IT-17')
ON CONFLICT DO NOTHING;

INSERT INTO user_info (is_account_locked, password, phone_number, role, username) VALUES
    (false, '$2a$10$29c7FqNe945nvsQzTpKuxO.ZaSmCVbGU9vFgQhDpPDjO3Ip5ggVUe', 'username', 'ADMINISTRATOR', 'username'),
    (false, '$2a$10$Vub3M8Uqy/6hnQkyzIKz9uzO3AUp6dJVrBPuL2blfJaBYx3hHc.ES', 'username1', 'ADMINISTRATOR', 'username1'),
    (false, '$2a$10$zxX4AZPN3I32t9isvpsYJOs1sGXEIdi3qzjCT0I3SXNUPUb8TjjX.', 'username2', 'ADMINISTRATOR', 'username2'),
    (false, '$2a$10$seChzroHn6eCNk/H7n.3Qu5amP5WsYVz05I6fZs97Kph9rqTS6vcm', 'username3', 'ADMINISTRATOR', 'username3'),
    (false, '$2a$10$DFWuQWujrRXD9fj/PyK3qO3Kmzp97ByXb./qQMCG2JAwmRLtQNK2C', 'username4', 'ADMINISTRATOR', 'username4'),
    (false, '$2a$10$BDMzrKT30nY0HFasmDcDZ.YSdlpaTjMXdMMZxYonXuuWdQ4VSdvV2', '1', 'ADMINISTRATOR', 'user')
ON CONFLICT DO NOTHING;


INSERT INTO users(fullname, info) VALUES
    ('Ненахов Евгений Валентинович', 1),
    ('Крылов Сергей Сергеевич',      2),
    ('Булакина Мария Борисовна',     3),
    ('Ирбитский Илья Сергеевич',     4),
    ('Погосян Михаил Асланович',     5),
    ('Грубенко Максим Дмитриевич',   6)
ON CONFLICT DO NOTHING;



INSERT Into tag(full_name, short_name, color)
VALUES
    ('Лекция', 'ЛК', '#FFD1DC' ),
    ('Практическое занятие', 'ПЗ', '#AEDFF7'),
    ('Лабораторная работа', 'ЛР', '#D7A9E3'),
    ('Экзамен', 'Экзамен', '#FFD8B1'),
    ('Служебное', 'Служебное', '#B8B8B8'),
    ('Мероприятие', 'Мероприятие', '#FFE7C4'),
    ('Совещание', 'Совещание', '#A8D8B9')
ON CONFLICT DO NOTHING;


INSERT INTO groups(name)
VALUES
    ('Группа 1'),
    ('Группа 2'),
    ('Группа 3'),
    ('Группа 4')
ON CONFLICT DO NOTHING;


INSERT INTO bookings (booking_group_id, room_id, owner_id, tag_id, start_time, end_time, title, description)
VALUES
    (null, 5, 3, 6, '2023-10-24 10:00:00.000', '2023-10-24 11:30:00.000', 'ДОД маи', 'Совещание по проведению дня открытых дверей в МАИ'),
    (null, 6, 2, 4, '2023-10-23 20:30:00.000', '2023-10-23 21:00:00.000', 'Онлайн магистратура', 'Организационное собрание в онлайн-магистратуре'),
    (null, 4, 1, 1, '2023-10-23 10:30:00.000', '2023-10-23 12:15:00.000', 'РПКС', 'Лекция по предмету "Разработка прикладных компьютерных систем"'),
    (null, 4, 1, 2, '2023-10-23 13:00:00.000', '2023-10-23 16:15:00.000', 'Архитектура информационных систем', 'Практические занятия по предмету "Архитектура информационных систем"'),
    (null, 9, 4, 3,'2023-10-23 13:00:00.000', '2023-10-23 16:15:00.000', 'Основы криптографии', 'Лабораторные работы по предмету "Основы криптографии"'),
    (null, 4, 5, 1,'2023-10-24 18:00:00.000', '2023-10-24 21:00:00.000', 'Лекция с ректором', 'Лекция Михаила Аслановича Погосяна'),
    (null, 4, 5, 6, '2023-10-24 18:00:00.000', '2023-10-24 21:00:00.000', 'Какое-то совещание', 'Совещание и еще много много текста про само мероприятие'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 1, 1, 6, '2023-10-25 15:00:00.000', '2023-10-25 17:00:00.000', 'Какое-то совещание', 'Совещание 1 и еще много много текста про само мероприятие'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 1, 1, 6, '2023-11-08 15:00:00.000', '2023-11-08 17:00:00.000', 'Какое-то совещание', 'Совещание 1 и еще много много текста про само мероприятие'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 1, 1, 6, '2023-11-22 15:00:00.000', '2023-11-22 17:00:00.000', 'Какое-то совещание', 'Совещание 1 и еще много много текста про само мероприятие'),
    ('15a08dac-b262-45d7-b0ef-90fcfcff37b5', 1, 1, 6, '2023-12-06 15:00:00.000', '2023-12-06 17:00:00.000', 'Какое-то совещание', 'Совещание 1 и еще много много текста про само мероприятие'),
    ('15d0e401-b7ae-46df-bdce-1bb72f214ebd', 2, 1, 6, '2023-12-06 15:00:00.000', '2023-12-06 17:00:00.000', 'Какое-то совещание', 'Совещание 2 и еще много много текста про само мероприятие')
ON CONFLICT DO NOTHING;

INSERT INTO bookings_groups(booking_id, groups_id)
VALUES
    (1,1), (1,2), (1,3),
    (2,4), (2,2)
ON CONFLICT DO NOTHING;

INSERT INTO bookings_staff(booking_id, staff_user_id)
VALUES
    (1,1),
    (2,4), (2,2), (2,5),
    (10,4), (10,2), (10,5), (10,1)
ON CONFLICT DO NOTHING;

