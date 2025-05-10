-- theme 테이블 데이터
INSERT INTO theme (name, description, thumbnail) VALUES
('좀비', 'Find clues and escape the zombie lab.', 'zombie.jpg'),
('공포', 'Solve the mystery of the lost civilization.', 'temple.jpg'),
('우주', 'Escape the failing space station before it explodes.', 'space.jpg');

-- reservation_time 테이블 데이터
INSERT INTO reservation_time (start_at) VALUES
('10:00'),
('13:00'),
('16:00');

-- member 테이블 데이터
INSERT INTO member (email, name, password, role) VALUES
('qwe', '다로', '1234', 'admin'),
('asd', '포로', '1234', 'user'),
('admin@example.com', 'Admin', 'admin1234', 'user');

-- reservation 테이블 데이터
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES
(1, '2025-03-12', 1, 2),
(2, '2025-03-16', 2, 1),
(3, '2025-03-17', 3, 3);
