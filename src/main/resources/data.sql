INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');

INSERT INTO theme (name, description, thumbnail) VALUES ('에버', '공포', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('배키', '스릴러', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (name, email, password) VALUES ('에버', 'treeboss@gmail.com', '123');
INSERT INTO member (name, email, password) VALUES ('우테코', 'wtc@gmail.com', '123');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-08', 1, 1, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-08', 2, 2, 2);
