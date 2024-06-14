INSERT INTO reservation_time (start_at)
VALUES ('01:00:00'),
       ('02:00:00'),
       ('09:00:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('Theme1', 'Description for Theme1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme2', 'Description for Theme2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme3', 'Description for Theme3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (name, email, password, is_admin)
VALUES ('pond', 'pond@naver.com', '123', false),
       ('admin', 'admin@naver.com', '123', true);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-05-10', 1, 1, 1),
       ('2024-05-11', 2, 1, 1);
