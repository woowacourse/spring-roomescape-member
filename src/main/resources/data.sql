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

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES
-- theme_id 1: 10건
('user1', CURRENT_DATE - 3, 13, 1),
('user2', CURRENT_DATE - 3, 12, 1),
('user3', CURRENT_DATE - 3, 11, 1),
('user4', CURRENT_DATE - 3, 10, 1),
('user5', CURRENT_DATE - 3, 9, 1),
('user6', CURRENT_DATE - 3, 8, 1),
('user7', CURRENT_DATE - 3, 7, 1),
('user8', CURRENT_DATE - 3, 6, 1),
('user9', CURRENT_DATE - 3, 5, 1),
('user10', CURRENT_DATE - 3, 4, 1),

-- theme_id 2: 9건
('user1', CURRENT_DATE - 2, 11, 2),
('user2', CURRENT_DATE - 2, 10, 2),
('user3', CURRENT_DATE - 2, 9, 2),
('user4', CURRENT_DATE - 2, 8, 2),
('user5', CURRENT_DATE - 2, 7, 2),
('user6', CURRENT_DATE - 2, 6, 2),
('user7', CURRENT_DATE - 2, 5, 2),
('user8', CURRENT_DATE - 2, 4, 2),
('user9', CURRENT_DATE - 2, 3, 2),

-- theme_id 3: 8건
('user1', CURRENT_DATE - 1, 9, 3),
('user2', CURRENT_DATE - 1, 8, 3),
('user3', CURRENT_DATE - 1, 7, 3),
('user4', CURRENT_DATE - 1, 6, 3),
('user5', CURRENT_DATE - 1, 5, 3),
('user6', CURRENT_DATE - 1, 4, 3),
('user7', CURRENT_DATE - 1, 3, 3),
('user8', CURRENT_DATE - 1, 2, 3),

-- theme_id 4: 7건
('user1', CURRENT_DATE - 7, 7, 4),
('user2', CURRENT_DATE - 7, 6, 4),
('user3', CURRENT_DATE - 7, 5, 4),
('user4', CURRENT_DATE - 7, 4, 4),
('user5', CURRENT_DATE - 7, 3, 4),
('user6', CURRENT_DATE - 7, 2, 4),
('user7', CURRENT_DATE - 7, 1, 4),

-- theme_id 5: 6건
('user1', CURRENT_DATE - 6, 6, 5),
('user2', CURRENT_DATE - 6, 5, 5),
('user3', CURRENT_DATE - 6, 4, 5),
('user4', CURRENT_DATE - 6, 3, 5),
('user5', CURRENT_DATE - 6, 2, 5),
('user6', CURRENT_DATE - 6, 1, 5),

-- theme_id 6: 5건
('user1', CURRENT_DATE - 5, 5, 6),
('user2', CURRENT_DATE - 5, 4, 6),
('user3', CURRENT_DATE - 5, 3, 6),
('user4', CURRENT_DATE - 5, 2, 6),
('user5', CURRENT_DATE - 5, 1, 6),

-- theme_id 7: 4건
('user1', CURRENT_DATE - 4, 4, 7),
('user2', CURRENT_DATE - 4, 3, 7),
('user3', CURRENT_DATE - 4, 2, 7),
('user4', CURRENT_DATE - 4, 1, 7),

-- theme_id 8: 3건
('user1', CURRENT_DATE - 3, 3, 8),
('user2', CURRENT_DATE - 3, 2, 8),
('user3', CURRENT_DATE - 3, 1, 8),

-- theme_id 9: 2건
('user1', CURRENT_DATE - 2, 2, 9),
('user2', CURRENT_DATE - 2, 1, 9),

-- theme_id 10: 1건
('user1', CURRENT_DATE - 1, 1, 10);






