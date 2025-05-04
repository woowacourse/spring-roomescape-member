INSERT INTO THEME (name, description, thumbnail) values ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO THEME (name, description, thumbnail) values ('레벨3 탈출', '우테코 레벨3를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO RESERVATION_TIME (start_at) values ('10:10');

INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('user1', '2025-05-04', 1, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('user1', '2025-05-05', 1, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('user1', '2025-05-06', 1, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('user1', '2025-05-07', 1, 1);

INSERT INTO RESERVATION (name, date, time_id, theme_id) values ('user1', '2025-05-03', 1, 2);
