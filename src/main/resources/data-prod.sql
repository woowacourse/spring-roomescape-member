INSERT INTO theme (name, description, thumbnail) VALUES ('테마 1', '설명 1', 'url 1');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마 2', '설명 2', 'url 2');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마 3', '설명 3', 'url 3');

INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('15:00');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation1', '2024-04-24', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation2', '2024-04-25', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation3', '2024-04-26', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation4', '2024-04-27', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation5', '2024-04-28', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation6', '2024-04-29', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation7', '2024-04-30', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation8', '2024-05-01', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation9', '2024-05-02', 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('reservation10', '2024-05-02', 1, 3);
