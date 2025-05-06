INSERT INTO reservation_time(start_at) VALUES
('10:00'),
('11:00');

INSERT INTO theme(theme_name, description, thumbnail) VALUES
('test', 'description', 'thumbnail'),
('test2', 'description2', 'thumbnail2');

INSERT INTO reservation(name, date, time_id, theme_id) VALUES
('test', '2025-01-01', 1, 1);
