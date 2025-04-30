INSERT INTO reservation_time(start_at) VALUES ('12:00')
INSERT INTO reservation_time(start_at) VALUES ('13:00')

INSERT INTO theme(name, description, thumbnail) VALUES ('test1', 'description1', 'thumbnail1')
INSERT INTO theme(name, description, thumbnail) VALUES ('test2', 'description1', 'thumbnail1')

INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('test1', '2025-04-21', 2, 2)
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('test1', '2025-05-01', 1, 1)