INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '테마 1입니다', 'thumbnail.jpg'),
       ('테마2', '테마 2입니다', 'thumbnail.jpg'),
       ('테마3', '테마 3입니다', 'thumbnail.jpg'),
       ('테마4', '테마 4입니다', 'thumbnail.jpg'),
       ('테마5', '테마 5입니다', 'thumbnail.jpg'),
       ('테마6', '테마 6입니다', 'thumbnail.jpg'),
       ('테마7', '테마 7입니다', 'thumbnail.jpg'),
       ('테마8', '테마 8입니다', 'thumbnail.jpg'),
       ('테마9', '테마 9입니다', 'thumbnail.jpg'),
       ('테마10', '테마 10입니다', 'thumbnail.jpg'),
       ('테마11', '테마 11입니다', 'thumbnail.jpg');

INSERT INTO members(email, password, name, role)
VALUES ('admin@email.com', 'password', '관리자', 'ADMIN'),
       ('user@email.com', 'password', '유저1', 'USER'),
       ('user2@email.com', 'password', '유저2', 'USER');

INSERT INTO reservation (member_id, theme_id, date, time_id)
VALUES (2, 1, '2025-04-29', 1),
       (2, 1, '2025-04-28', 2),
       (2, 1, '2025-04-27', 1),
       (2, 1, '2025-04-26', 1),
       (2, 1, '2025-04-25', 1),
       (2, 2, '2025-04-24', 1),
       (2, 2, '2025-04-23', 1),
       (2, 2, '2025-04-23', 2),
       (2, 2, '2025-04-23', 3),
       (3, 8, '2025-04-23', 4),
       (3, 8, '2025-04-23', 1),
       (3, 8, '2025-04-23', 2),
       (3, 9, '2025-04-23', 1),
       (3, 11, '2025-04-23', 1),
       (3, 11, '2025-04-23', 4);
