INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '재밌음', '/image/default.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '무서움', '/image/default.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마3', '놀라움', '/image/default.jpg');

INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('테스트1', '2025-04-28', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('테스트2', '2025-04-28', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('테스트3', '2025-04-26', 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('테스트4', '2025-04-18', 1, 2);
