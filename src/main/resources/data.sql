INSERT INTO user_info (username, phone_number, password, is_account_locked, role)
VALUES  ('user', '+79123456789', 'root',      FALSE, 'AUTHORISED'),
        ('admin', '+79234567890', 'root', FALSE, 'ADMINISTRATOR'),
        ('teacher', '+79345678901', 'root', FALSE, 'TEACHER')
ON CONFLICT DO NOTHING;

INSERT INTO users (fullname, info)
VALUES  ('Смирнов Александр Иванович', 1),
        ('Жукова Мария Александровна', 2),
        ('Григорьев Артем Васильевич', 3)
ON CONFLICT DO NOTHING;

INSERT INTO rooms (room_name, capacity, has_computers, has_projector, is_cathedral)
VALUES  ('Room 101', 50, TRUE, TRUE, TRUE),
        ('Room 102', 25, TRUE, TRUE, TRUE),
        ('Room 103', 75, TRUE, TRUE, TRUE),
        ('Room 104', 25, TRUE, TRUE, TRUE),
        ('Room 105', 11, TRUE, TRUE, TRUE),
        ('Room 106', 25, TRUE, TRUE, TRUE),
        ('Room 107', 50, TRUE, TRUE, TRUE)
ON CONFLICT DO NOTHING;

INSERT INTO tag (full_name, short_name, color)
VALUES  ('ПЗ', 'Практическое Занятие', '#0000FF'),
        ('ЛК', 'Лекционное Занятие', '#FF0000'),
        ('ЛР', 'Лабораторная рабта', '#008000')
ON CONFLICT DO NOTHING;