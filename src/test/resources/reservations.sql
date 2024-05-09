INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '테마1 설명', 'https://via.placeholder.com/150/92c952'),
       ('테마2', '테마2 설명', 'https://via.placeholder.com/150/771796'),
       ('테마3', '테마3 설명', 'https://via.placeholder.com/150/24f355'),
       ('테마4', '테마4 설명', 'https://via.placeholder.com/150/30f9e7'),
       ('테마5', '테마5 설명', 'https://via.placeholder.com/150/56a8c2');

INSERT INTO reservation_time (start_at)
VALUES ('09:00'),
       ('12:00'),
       ('15:00'),
       ('18:00'),
       ('21:00');

INSERT INTO member (email, password, name)
VALUES ('qwer@naver.com', '1234', '구름');

INSERT INTO reservation (date, member_id, time_id, theme_id)
VALUES ('2024-04-1', 1, 1, 5),
       ('2024-04-1', 1, 2, 5),
       ('2024-04-1', 1, 3, 5),
       ('2024-04-2', 1, 1, 5),
       ('2024-04-3', 1, 2, 5),
       ('2024-04-3', 1, 3, 4),
       ('2024-04-4', 1, 4, 4),
       ('2024-04-4', 1, 1, 4),
       ('2024-04-4', 1, 2, 4),
       ('2024-04-5', 1, 1, 3),
       ('2024-04-5', 1, 2, 3),
       ('2024-04-5', 1, 3, 3),
       ('2024-04-6', 1, 1, 2),
       ('2024-04-6', 1, 2, 2),
       ('2024-04-6', 1, 3, 1);

