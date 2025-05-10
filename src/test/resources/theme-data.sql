INSERT INTO theme(name, description, thumbnail)
VALUES ('test', 'test', 'test'),
       ('test1', 'test', 'test'),
       ('test2', 'test', 'test');

INSERT INTO reservation_time(start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00');

INSERT INTO member(name, email, password, role)
VALUES ('test', 'email@email.com', 'password', 'USER');

INSERT INTO reservation(date, time_id, theme_id, member_id)
VALUES ('2025-4-25', 1, 1, 1);

