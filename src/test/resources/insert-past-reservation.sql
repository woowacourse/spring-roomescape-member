INSERT INTO theme(name, description, thumbnail) VALUES ('테마1', '설명1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES ('테마2', '설명2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES ('테마3', '설명3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation_time(start_at) VALUES ('10:00');

INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('리니', DATEADD('DAY', -1, CURRENT_DATE), 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('릴리', DATEADD('DAY', -7, CURRENT_DATE), 1, 2);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('토미', DATEADD('DAY', -8, CURRENT_DATE), 1, 3);
