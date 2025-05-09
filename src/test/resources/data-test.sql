-- reservation_time 데이터 삽입
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

-- theme 데이터 삽입
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

-- member 데이터 삽입
INSERT INTO member (id, name, email, password)
VALUES (1, 'user1', 'user1@example.com', 'user1123'),
       (2, 'user2', 'user2@example.com', 'user2123'),
       (3, 'user3', 'user3@example.com', 'user3123'),
       (4, 'user4', 'user4@example.com', 'user4123'),
       (5, 'user5', 'user5@example.com', 'user5123'),
       (6, 'user6', 'user6@example.com', 'user6123'),
       (7, 'user7', 'user7@example.com', 'user7123'),
       (8, 'user8', 'user8@example.com', 'user8123'),
       (9, 'user9', 'user9@example.com', 'user9123'),
       (10, 'user10', 'user10@example.com', 'user10123');

-- reservation 데이터 삽입
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES
-- theme_id 1: 10건
(CURRENT_DATE - 3, 13, 1, 1),
(CURRENT_DATE - 3, 12, 1, 2),
(CURRENT_DATE - 3, 11, 1, 3),
(CURRENT_DATE - 3, 10, 1, 4),
(CURRENT_DATE - 3, 9, 1, 5),
(CURRENT_DATE - 3, 8, 1, 6),
(CURRENT_DATE - 3, 7, 1, 7),
(CURRENT_DATE - 3, 6, 1, 8),
(CURRENT_DATE - 3, 5, 1, 9),
(CURRENT_DATE - 3, 4, 1, 10),

-- theme_id 2: 9건
(CURRENT_DATE - 2, 11, 2, 1),
(CURRENT_DATE - 2, 10, 2, 2),
(CURRENT_DATE - 2, 9, 2, 3),
(CURRENT_DATE - 2, 8, 2, 4),
(CURRENT_DATE - 2, 7, 2, 5),
(CURRENT_DATE - 2, 6, 2, 6),
(CURRENT_DATE - 2, 5, 2, 7),
(CURRENT_DATE - 2, 4, 2, 8),
(CURRENT_DATE - 2, 3, 2, 9),

-- theme_id 3: 8건
(CURRENT_DATE - 1, 9, 3, 1),
(CURRENT_DATE - 1, 8, 3, 2),
(CURRENT_DATE - 1, 7, 3, 3),
(CURRENT_DATE - 1, 6, 3, 4),
(CURRENT_DATE - 1, 5, 3, 5),
(CURRENT_DATE - 1, 4, 3, 6),
(CURRENT_DATE - 1, 3, 3, 7),
(CURRENT_DATE - 1, 2, 3, 8),

-- theme_id 4: 7건
(CURRENT_DATE - 7, 7, 4, 1),
(CURRENT_DATE - 7, 6, 4, 2),
(CURRENT_DATE - 7, 5, 4, 3),
(CURRENT_DATE - 7, 4, 4, 4),
(CURRENT_DATE - 7, 3, 4, 5),
(CURRENT_DATE - 7, 2, 4, 6),
(CURRENT_DATE - 7, 1, 4, 7),

-- theme_id 5: 6건
(CURRENT_DATE - 6, 6, 5, 1),
(CURRENT_DATE - 6, 5, 5, 2),
(CURRENT_DATE - 6, 4, 5, 3),
(CURRENT_DATE - 6, 3, 5, 4),
(CURRENT_DATE - 6, 2, 5, 5),
(CURRENT_DATE - 6, 1, 5, 6),

-- theme_id 6: 5건
(CURRENT_DATE - 5, 5, 6, 1),
(CURRENT_DATE - 5, 4, 6, 2),
(CURRENT_DATE - 5, 3, 6, 3),
(CURRENT_DATE - 5, 2, 6, 4),
(CURRENT_DATE - 5, 1, 6, 5),

-- theme_id 7: 4건
(CURRENT_DATE - 4, 4, 7, 1),
(CURRENT_DATE - 4, 3, 7, 2),
(CURRENT_DATE - 4, 2, 7, 3),
(CURRENT_DATE - 4, 1, 7, 4),

(CURRENT_DATE - 3, 3, 8, 1),
(CURRENT_DATE - 3, 2, 8, 2),
(CURRENT_DATE - 3, 1, 8, 3),

(CURRENT_DATE - 2, 2, 9, 1),
(CURRENT_DATE - 2, 1, 9, 2),

-- theme_id 10: 1건
(CURRENT_DATE - 1, 1, 10, 1);
