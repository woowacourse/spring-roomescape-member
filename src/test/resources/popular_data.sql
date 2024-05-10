INSERT INTO member (name, email, password)
VALUES ('사용자1', 'user1@wooteco.com', 'user1');
INSERT INTO member (name, email, password)
VALUES ('사용자2', 'user2@wooteco.com', 'user2');
INSERT INTO member (name, email, password)
VALUES ('사용자3', 'user3@wooteco.com', 'user3');
INSERT INTO member (name, email, password)
VALUES ('사용자4', 'user4@wooteco.com', 'user4');
INSERT INTO member (name, email, password)
VALUES ('사용자5', 'user5@wooteco.com', 'user5');

INSERT INTO reservation_time (start_at)
VALUES ('10:00');
INSERT INTO reservation_time (start_at)
VALUES ('11:00');
INSERT INTO reservation_time (start_at)
VALUES ('12:00');
INSERT INTO reservation_time (start_at)
VALUES ('13:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00');
INSERT INTO reservation_time (start_at)
VALUES ('15:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출1', '1번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출2', '2번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출3', '3번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출4', '4번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출5', '5번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail)
VALUES ('방탈출6', '6번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출7', '7번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출8', '8번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출9', '9번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출10', '10번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출11', '11번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출12', '12번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출13', '13번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출14', '14번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출15', '15번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (date, member_id, time_id, theme_id)
VALUES ('2024-05-03', 1, 1, 1),
       ('2024-05-03', 2, 2, 1),
       ('2024-05-03', 3, 3, 1),
       ('2024-05-03', 4, 4, 1),
       ('2024-05-03', 5, 5, 1),
       ('2024-05-04', 1, 1, 2),
       ('2024-05-04', 2, 2, 2),
       ('2024-05-04', 3, 3, 2),
       ('2024-05-04', 4, 4, 2),
       ('2024-05-05', 1, 1, 3),
       ('2024-05-05', 2, 2, 3),
       ('2024-05-05', 3, 3, 3),
       ('2024-05-06', 1, 1, 4),
       ('2024-05-06', 2, 2, 4),
       ('2024-05-07', 1, 1, 5),
       ('2024-05-07', 2, 2, 6),
       ('2024-05-08', 1, 1, 7),
       ('2024-05-09', 1, 1, 8),
       ('2024-05-03', 1, 1, 9),
       ('2024-05-04', 2, 2, 10),
       ('2024-05-05', 3, 3, 11),
       ('2024-05-06', 4, 4, 12);
