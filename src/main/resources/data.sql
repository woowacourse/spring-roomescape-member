INSERT INTO member (email, password, name, role)
VALUES ('admin@email.com', 'password', '관리자', 'ADMIN'),
       ('member1@email.com', '1234', '회원1', 'USER'),
       ('member2@email.com', '1234', '회원2', 'USER'),
       ('member3@email.com', '1234', '회원3', 'USER'),
       ('member4@email.com', '1234', '회원4', 'USER'),
       ('member5@email.com', '1234', '회원5', 'USER'),
       ('member6@email.com', '1234', '회원6', 'USER'),
       ('member7@email.com', '1234', '회원7', 'USER'),
       ('member8@email.com', '1234', '회원8', 'USER'),
       ('member9@email.com', '1234', '회원9', 'USER'),
       ('member10@email.com', '1234', '회원10', 'USER');

INSERT INTO reservation_time (id, start_at)
VALUES (1, '09:00'),
       (2, '10:00'),
       (3, '11:00'),
       (4, '12:00'),
       (5, '13:00'),
       (6, '14:00'),
       (7, '15:00'),
       (8, '16:00'),
       (9, '17:00'),
       (10, '18:00'),
       (11, '19:00'),
       (12, '20:00'),
       (13, '21:00'),
       (14, '22:00'),
       (15, '23:00');

INSERT INTO theme (id, name, description, thumbnail)
VALUES (1, '우테코 레벨1 탈출', '우테코 레벨1 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (2, '우테코 레벨2 탈출', '우테코 레벨2 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (3, '우테코 레벨3 탈출', '우테코 레벨3 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (4, '우테코 레벨4 탈출', '우테코 레벨4 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (5, '우테코 레벨5 탈출', '우테코 레벨5 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (6, '우테코 레벨6 탈출', '우테코 레벨6 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (7, '우테코 레벨7 탈출', '우테코 레벨7 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (8, '우테코 레벨8 탈출', '우테코 레벨8 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (9, '우테코 레벨9 탈출', '우테코 레벨9 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (10, '우테코 레벨10 탈출', '우테코 레벨10 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (11, '우테코 레벨11 탈출', '우테코 레벨11 탈출 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES
-- theme_id 1: 10건
(2, CURRENT_DATE - 3, 13, 1),
(3, CURRENT_DATE - 3, 12, 1),
(4, CURRENT_DATE - 3, 11, 1),
(5, CURRENT_DATE - 3, 10, 1),
(6, CURRENT_DATE - 3, 9, 1),
(7, CURRENT_DATE - 3, 8, 1),
(8, CURRENT_DATE - 3, 7, 1),
(9, CURRENT_DATE - 3, 6, 1),
(10, CURRENT_DATE - 3, 5, 1),
(11, CURRENT_DATE - 3, 4, 1),

-- theme_id 2: 9건
(2, CURRENT_DATE - 2, 11, 2),
(3, CURRENT_DATE - 2, 10, 2),
(4, CURRENT_DATE - 2, 9, 2),
(5, CURRENT_DATE - 2, 8, 2),
(6, CURRENT_DATE - 2, 7, 2),
(7, CURRENT_DATE - 2, 6, 2),
(8, CURRENT_DATE - 2, 5, 2),
(9, CURRENT_DATE - 2, 4, 2),
(10, CURRENT_DATE - 2, 3, 2),

-- theme_id 3: 8건
(2, CURRENT_DATE - 1, 9, 3),
(3, CURRENT_DATE - 1, 8, 3),
(4, CURRENT_DATE - 1, 7, 3),
(5, CURRENT_DATE - 1, 6, 3),
(6, CURRENT_DATE - 1, 5, 3),
(7, CURRENT_DATE - 1, 4, 3),
(8, CURRENT_DATE - 1, 3, 3),
(9, CURRENT_DATE - 1, 2, 3),

-- theme_id 4: 7건
(2, CURRENT_DATE - 7, 7, 4),
(3, CURRENT_DATE - 7, 6, 4),
(4, CURRENT_DATE - 7, 5, 4),
(5, CURRENT_DATE - 7, 4, 4),
(6, CURRENT_DATE - 7, 3, 4),
(7, CURRENT_DATE - 7, 2, 4),
(8, CURRENT_DATE - 7, 1, 4),

-- theme_id 5: 6건
(2, CURRENT_DATE - 6, 6, 5),
(3, CURRENT_DATE - 6, 5, 5),
(4, CURRENT_DATE - 6, 4, 5),
(5, CURRENT_DATE - 6, 3, 5),
(6, CURRENT_DATE - 6, 2, 5),
(7, CURRENT_DATE - 6, 1, 5),

-- theme_id 6: 5건
(2, CURRENT_DATE - 5, 5, 6),
(3, CURRENT_DATE - 5, 4, 6),
(4, CURRENT_DATE - 5, 3, 6),
(5, CURRENT_DATE - 5, 2, 6),
(6, CURRENT_DATE - 5, 1, 6),

-- theme_id 7: 4건
(2, CURRENT_DATE - 4, 4, 7),
(3, CURRENT_DATE - 4, 3, 7),
(4, CURRENT_DATE - 4, 2, 7),
(5, CURRENT_DATE - 4, 1, 7),

-- theme_id 8: 3건
(2, CURRENT_DATE - 3, 3, 8),
(3, CURRENT_DATE - 3, 2, 8),
(4, CURRENT_DATE - 3, 1, 8),

-- theme_id 9: 2건
(2, CURRENT_DATE - 2, 2, 9),
(3, CURRENT_DATE - 2, 1, 9),

-- theme_id 10: 1건
(2, CURRENT_DATE - 1, 1, 10);




