INSERT INTO member(name, email, password, role) VALUES
('test', 'test@email.com', '1234', 'MEMBER'),
('test2', 'test2@email.com', '12345', 'MEMBER');

INSERT INTO reservation_time(start_at) VALUES
('10:00'),
('11:00');

INSERT INTO theme(theme_name, description, thumbnail) VALUES
('test', 'description', 'thumbnail'),
('test2', 'description2', 'thumbnail2');

INSERT INTO reservation(member_id, date, time_id, theme_id) VALUES
(1, '2025-01-01', 1, 1),
(1, '2025-01-02', 1, 1),
(1, '2025-01-03', 1, 2),
(1, '2025-01-04', 1, 2),
(1, '2025-01-05', 1, 2),
(2, '2025-01-01', 1, 1),
(2, '2025-01-02', 1, 1),
(2, '2025-01-03', 1, 1),
(2, '2025-01-04', 1, 2),
(2, '2025-01-05', 1, 2);
