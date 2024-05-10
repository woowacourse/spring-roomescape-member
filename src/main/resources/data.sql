INSERT INTO reservation_time(start_at) VALUES ('10:00:00');
INSERT INTO reservation_time(start_at) VALUES ('19:00:00');

INSERT INTO theme(name, description, thumbnail) VALUES ('레벨2 탈출', '우테코 레벨2 탈출기!', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES ('레벨3 탈출', '우테코 레벨3 탈출기!', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES ('레벨4 탈출', '우테코 레벨4 탈출기!', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member(name, email) VALUES ('브리', 'bri@abc.com');
INSERT INTO member(name, email) VALUES ('브라운', 'brown@abc.com');
INSERT INTO member(name, email) VALUES ('오리', 'duck@abc.com');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('브라운', CURRENT_DATE - 1, 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('브리', CURRENT_DATE - 2, 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('오리', CURRENT_DATE - 2, 2, 2);
