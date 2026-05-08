INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');

INSERT INTO theme (name, description, thumbnail_url) VALUES
('세기의 도둑', '보안을 뚫고 보석을 훔쳐라', 'https://example.com/themes/time.jpg'),
('세기의 도둑2', '보안을 뚫고 보석을 훔쳐라2', 'https://example.com/themes2/time.jpg'),
('세기의 도둑3', '보안을 뚫고 보석을 훔쳐라3', 'https://example.com/themes3/time.jpg'),
('세기의 도둑4', '보안을 뚫고 보석을 훔쳐라4', 'https://example.com/themes4/time.jpg');

INSERT INTO schedule (date, time_id, theme_id) VALUES
('2026-05-05', 1, 1), -- id=1
('2026-05-05', 2, 2), -- id=2
('2026-05-05', 3, 3), -- id=3
('2026-05-05', 4, 4), -- id=4
('2026-05-06', 2, 2); -- id=5

INSERT INTO reservation (name, schedule_id) VALUES
('a', 1),
('b', 2),
('c', 3),
('d', 5);
