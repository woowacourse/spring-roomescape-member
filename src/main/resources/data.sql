INSERT INTO reservation_time(id, start_at) VALUES(1, '10:00');
INSERT INTO reservation_time(id, start_at) VALUES(2, '15:00');

INSERT INTO theme(id, name, description, thumbnail) VALUES(1, '테마1', '테마1입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(2, '테마2', '테마2입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(3, '테마3', '테마3입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(4, '테마4', '테마4입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(5, '테마5', '테마5입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(6, '테마6', '테마6입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(7, '테마7', '테마7입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(8, '테마8', '테마8입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(9, '테마9', '테마9입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(10, '테마10', '테마10입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(id, name, description, thumbnail) VALUES(11, '테마11', '테마11입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES(1, '사용자1', '2025-05-01', 1, 11);
INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES(2, '사용자1', '2025-05-01', 2, 11);
INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES(3, '사용자1', '2025-04-30', 1, 11);
INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES(4, '사용자1', '2025-04-30', 2, 9);
INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES(5, '사용자1', '2025-04-29', 2, 9);
INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES(6, '사용자1', '2025-04-29', 1, 8);
