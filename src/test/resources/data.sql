INSERT INTO reservation_time(id, start_at)
VALUES (1, '10:00'),
       (2, '11:00'),
       (3, '12:00');

INSERT INTO theme(id, name, description, thumbnail)
VALUES (1, '테마1', '설명1', 'thumbnail1.jpg'),
       (2, '테마2', '설명2', 'thumbnail2.jpg'),
       (3, '테마3', '설명3', 'thumbnail3.jpg');

INSERT INTO member(name, email, password, role)
VALUES ('체체', 'email@email.com', 'password', 'USER');
INSERT INTO member(name, email, password, role)
VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN');

INSERT INTO reservation(date, time_id, theme_id, member_id)
VALUES ('2025-04-25', 1, 1, 1),
       ('2025-04-25', 1, 2, 1);

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 4;

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 4;

ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 3;

ALTER TABLE member
    ALTER COLUMN id RESTART WITH 2;
