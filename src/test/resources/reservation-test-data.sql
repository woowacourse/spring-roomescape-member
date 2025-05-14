INSERT INTO member(name, email, password, role) VALUES
('member', 'member@email.com', '1234', 'MEMBER'),
('admin', 'admin@email.com', '1234', 'ADMIN');

INSERT INTO reservation_time(start_at) VALUES
('10:00'),
('11:00');

INSERT INTO theme(theme_name, description, thumbnail) VALUES
('test', 'description', 'thumbnail'),
('test2', 'description2', 'thumbnail2');

INSERT INTO reservation(member_id, date, time_id, theme_id) VALUES
(1, '2025-01-01', 1, 1);
