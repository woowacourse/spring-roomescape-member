INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('10:10');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('15:30');
INSERT INTO reservation_time (start_at) VALUES ('18:00');
INSERT INTO reservation_time (start_at) VALUES ('20:23');

INSERT INTO theme (name, description, thumbnail) VALUES ('레벨1 탈출', '우테코 레벨1를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('레벨3 탈출', '우테코 레벨3를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('루키', '2025-05-05', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('슬링키', '2025-05-12', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('범블비', '2025-05-18', 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('부기', '2025-05-20', 4, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('시소', '2025-05-24', 5, 2);
