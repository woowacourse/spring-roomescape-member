INSERT INTO theme (name, description, thumbnail) VALUES ('테마명01', '테마 설명01', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명02', '테마 설명02', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명03', '테마 설명03', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명04', '테마 설명04', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명05', '테마 설명05', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명06', '테마 설명06', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명07', '테마 설명07', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명08', '테마 설명08', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명09', '테마 설명09', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명10', '테마 설명10', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명11', '테마 설명11', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명12', '테마 설명12', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명13', '테마 설명13', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명14', '테마 설명14', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('테마명15', '테마 설명15', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('15:00');
INSERT INTO reservation_time (start_at) VALUES ('17:00');
INSERT INTO reservation_time (start_at) VALUES ('18:00');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', DATEADD(DAY, -7, CURDATE()), '1', '1');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저02', DATEADD(DAY, -7, CURDATE()), '2', '1');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', DATEADD(DAY, -7, CURDATE()), '3', '1');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저04', DATEADD(DAY, -7, CURDATE()), '4', '1');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', DATEADD(DAY, -7, CURDATE()), '3', '7');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', DATEADD(DAY, -7, CURDATE()), '3', '8');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', DATEADD(DAY, -7, CURDATE()), '3', '9');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', DATEADD(DAY, -7, CURDATE()), '3', '10');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', DATEADD(DAY, -6, CURDATE()), '1', '2');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저02', DATEADD(DAY, -6, CURDATE()), '2', '2');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', DATEADD(DAY, -6, CURDATE()), '3', '2');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', DATEADD(DAY, -5, CURDATE()), '1', '3');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저02', DATEADD(DAY, -5, CURDATE()), '2', '3');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', DATEADD(DAY, -5, CURDATE()), '3', '3');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', DATEADD(DAY, -4, CURDATE()), '1', '4');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저02', DATEADD(DAY, -4, CURDATE()), '2', '4');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', DATEADD(DAY, -3, CURDATE()), '1', '5');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저02', DATEADD(DAY, -3, CURDATE()), '2', '5');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', DATEADD(DAY, -2, CURDATE()), '1', '6');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', '2024-04-01', '1', '15');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', DATEADD(DAY, -1, CURDATE()), '1', '11');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저01', '2023-04-26', '1', '1');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저02', '2023-04-26', '2', '1');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', '2023-04-26', '3', '1');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저04', '2023-04-26', '4', '1');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', '2023-04-26', '3', '10');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', '2023-04-26', '1', '10');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', '2023-04-26', '2', '10');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('유저03', '2023-04-26', '4', '10');

INSERT INTO users (name, email, password) VALUES ('유저', 'admin@email.com', 'password');
