INSERT INTO member
    (name, email, password)
VALUES
    ('사용자1', 'test1@test.com', '1234');

INSERT INTO member
    (name, email, password)
VALUES
    ('사용자2', 'test2@test.com', '1234');

INSERT INTO member
    (name, email, password)
VALUES
    ('사용자3', 'test3@test.com', '1234');

INSERT INTO member
    (name, email, password)
VALUES
    ('사용자4', 'test4@test.com', '1234');

INSERT INTO reservation_time
    (start_at)
VALUES ('10:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('11:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('12:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('13:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('14:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('15:00');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출1', '1번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출2', '2번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출3', '3번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출4', '4번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출5', '5번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출6', '6번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출7', '7번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출8', '8번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출9', '9번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출10', '10번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출11', '11번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출12', '12번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출13', '13번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출14', '14번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출15', '15번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-06', 1, 5);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-06', 2, 5);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-07', 3, 5);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-07', 4, 5);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (3, '2024-05-07', 5, 5);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (3, '2024-05-07', 6, 4);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (4, '2024-05-08', 1, 4);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (4, '2024-05-08', 2, 4);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-08', 3, 4);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-09', 4, 3);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (3, '2024-05-09', 5, 3);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (4, '2024-05-09', 6, 3);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-10', 1, 2);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-10', 2, 2);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 3, 1);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 4, 7);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (3, '2024-05-11', 5, 8);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-11', 6, 9);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-12', 1, 10);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (3, '2024-05-12', 2, 11);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (4, '2024-04-23', 1, 4);

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-04-23', 2, 4);
