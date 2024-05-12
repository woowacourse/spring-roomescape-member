INSERT INTO reservation_time (start_at)
VALUES ('01:00:00'),
       ('09:00:00'),
       ('10:00:00'),
       ('11:00:00'),
       ('12:00:00'),
       ('13:00:00'),
       ('14:00:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('Theme1', 'Description for Theme1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme2', 'Description for Theme2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme3', 'Description for Theme3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme4', 'Description for Theme4', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme5', 'Description for Theme5', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme6', 'Description for Theme6', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-24', 1, 1, 1),
       ('2024-04-25', 1, 4, 1),
       ('2024-04-25', 2, 4, 1),
       ('2024-04-25', 3, 4, 1),
       ('2024-04-25', 3, 3, 1),
       ('2024-04-26', 3, 3, 1),
       ('2024-04-26', 4, 2, 1),
       ('2024-05-01', 1, 5, 1),
       ('2024-05-02', 6, 6, 1);

INSERT INTO member (name, email, password, is_admin)
VALUES ('pond', 'pond@naver.com', '123', false),
       ('admin', 'admin@naver.com', '123', true);
