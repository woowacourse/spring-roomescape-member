INSERT INTO member
    (name, email, password, role)
VALUES ('사용자1', 'test1@test.com', '1234', 'admin'),
       ('사용자2', 'test2@test.com', '1234', 'admin'),
       ('사용자3', 'test3@test.com', '1234', 'user'),
       ('사용자4', 'test4@test.com', '1234', 'user');

INSERT INTO reservation_time
    (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00'),
       ('14:00'),
       ('15:00');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출1', '1번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출2', '2번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출3', '3번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출4', '4번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출5', '5번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출6', '6번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출7', '7번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출8', '8번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출9', '9번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출10', '10번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출11', '11번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출12', '12번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출13', '13번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출14', '14번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('방탈출15', '15번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation
    (member_id, date, time_id, theme_id)
VALUES (1, '2024-04-25', 1, 5),
       (2, '2024-04-25', 2, 5),
       (3, '2024-04-25', 3, 5),
       (4, '2024-04-26', 4, 5),
       (1, '2024-04-26', 5, 5),
       (2, '2024-04-26', 6, 4),
       (1, '2024-04-27', 1, 4),
       (1, '2024-04-27', 2, 4),
       (1, '2024-04-27', 3, 4),
       (1, '2024-04-28', 4, 3),
       (1, '2024-04-28', 5, 3),
       (1, '2024-04-28', 6, 3),
       (1, '2024-04-29', 1, 2),
       (1, '2024-04-29', 2, 2),
       (1, '2024-04-29', 3, 1),
       (1, '2024-04-30', 4, 7),
       (1, '2024-04-30', 5, 8),
       (1, '2024-04-30', 6, 9),
       (1, '2024-05-01', 1, 10),
       (1, '2024-05-01', 2, 11),
       (1, '2024-04-23', 1, 4),
       (1, '2024-04-23', 2, 4);

