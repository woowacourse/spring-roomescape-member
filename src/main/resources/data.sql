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

INSERT INTO MEMBER (NAME, EMAIL, PASSWORD, ROLE)
VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
       ('유저', 'user@email.com', 'password', 'USER');

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2025-01-15', 1, 1, 1),
       ('2025-01-15', 3, 2, 2),
       ('2025-01-16', 2, 3, 1),
       ('2025-01-16', 4, 1, 2),
       ('2025-02-01', 5, 2, 1),
       ('2025-02-01', 6, 3, 2),
       ('2025-02-02', 1, 1, 1),
       ('2025-03-01', 2, 2, 2),
       ('2025-03-01', 3, 3, 1),
       ('2025-03-02', 4, 1, 2);
