INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('12:00'),
       ('14:00'),
       ('16:00'),
       ('18:00'),
       ('20:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '테마1입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마2', '테마2입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마3', '테마3입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (name, email, password)
VALUES ('김민준', 'minjun.kim@example.com', 'password123'),
       ('이서연', 'seoyeon.lee@example.com', 'password456'),
       ('박지훈', 'jihoon.park@example.com', 'password789'),
       ('최수아', 'sua.choi@example.com', 'password101'),
       ('정도윤', 'doyun.jung@example.com', 'password202'),
       ('강하은', 'haeun.kang@example.com', 'password303');

-- INSERT INTO reservation (name, date, time_id, theme_id)
-- VALUES ('김민준', '2025-05-03', 1, 3),
--        ('이서연', '2025-05-03', 3, 3),
--        ('박지훈', '2025-05-03', 2, 3),
--        ('최수아', '2025-05-03', 4, 2),
--        ('정도윤', '2025-05-03', 5, 2),
--        ('강하은', '2025-05-03', 6, 1);
