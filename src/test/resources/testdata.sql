INSERT INTO reservation_time(start_at)
VALUES ('10:30'),
       ('11:30'),
       ('12:30'),
       ('13:30'),
       ('14:30'),
       ('15:30'),
       ('16:30');

INSERT INTO theme(name, description, thumbnail)
VALUES ('테마 1', '테마 1입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 2', '테마 2입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 3', '테마 3입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 4', '테마 4입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 5', '테마 5입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 6', '테마 6입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 7', '테마 7입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 8', '테마 8입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 9', '테마 9입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 10', '테마 10입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('테마 11', '테마 11입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member(name, email, password, role)
VALUES ('뽀로로', '123@email.com', '123', 'user'),
       ('홍길동', '456@email.com', '456', 'user'),
       ('제이', '789@email.com', '789', 'admin'),
       ('잉크', 'test@email.com', 'test', 'user');

INSERT INTO reservation(date, time_id, theme_id, member_id)
VALUES ('2024-05-14', 1, 1, 1),
       ('2024-05-14', 2, 1, 2),
       ('2024-05-14', 3, 2, 3),
       ('2024-05-14', 4, 2, 4),
       ('2024-05-14', 5, 3, 1),
       ('2024-05-14', 6, 2, 2),
       ('2024-05-14', 7, 2, 3);
