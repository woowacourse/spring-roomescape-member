INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00');
INSERT INTO reservation_time (id, start_at) VALUES (2, '11:00');
INSERT INTO reservation_time (id, start_at) VALUES (3, '12:00');
ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 4;
