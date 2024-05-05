INSERT INTO reservation_time (start_at)
VALUES ('11:00:00'),
       ('12:00:00'),
       ('13:00:00'),
       ('14:00:00'),
       ('15:00:00')
;

INSERT INTO theme (name, description, thumbnail)
VALUES ('Theme1', 'Description for Theme1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme2', 'Description for Theme2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme3', 'Description for Theme3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme4', 'Description for Theme4', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Theme5', 'Description for Theme5', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg')
;

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('User1', '2024-04-24', 1, 1),
       ('User2', '2024-04-25', 1, 4),
       ('User3', '2024-04-25', 2, 4),
       ('User4', '2024-04-25', 3, 4),
       ('User5', '2024-04-25', 3, 3)
;
