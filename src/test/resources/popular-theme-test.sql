INSERT INTO member(name, email, password, role) VALUES
('test', 'test@email.com', '1234', 'MEMBER');

INSERT INTO reservation_time (time_id, start_at) VALUES
(100, '12:00'),
(200, '13:00');

INSERT INTO theme (theme_id, theme_name, description, thumbnail) VALUES
(100, 'top1', 'description1', 'thumbnail1'),
(200, 'top2', 'description2', 'thumbnail2'),
(300, 'top3', 'description3', 'thumbnail3'),
(400, 'notContain', 'description4', 'thumbnail4');

INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES
(1, '2025-05-01', 100, 100),
(1, '2025-05-02', 200, 100),
(1, '2025-05-03', 100, 100),
(1, '2025-05-04', 200, 100),

(1, '2025-05-05', 200, 200),
(1, '2025-05-06', 100, 200),
(1, '2025-05-07', 100, 200),

(1, '2025-05-06', 100, 300),
(1, '2025-05-07', 100, 300),

(1, '2025-04-30', 200, 400),
(1, '2025-05-08', 100, 400);
