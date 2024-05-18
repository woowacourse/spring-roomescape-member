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

INSERT INTO member (name, email, password, role) VALUES ('어드민', 'user@example.com', 'password1!', 'ADMIN');
INSERT INTO member (name, email, password, role) VALUES ('유저', 'user2@example.com', 'password2@', 'USER');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '1', '1', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '2', '1', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '3', '1', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '4', '1', '1');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '3', '7', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '3', '8', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '3', '9', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-12', '3', '10', '1');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-13', '1', '2', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-13', '2', '2', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-13', '3', '2', '1');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-14', '1', '3', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-14', '2', '3', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-14', '3', '3', '1');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-15', '1', '4', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-15', '2', '4', '1');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-16', '1', '5', '1');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-16', '2', '5', '1');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-17', '1', '6', '1');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-17', '1', '15', '2');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-17', '1', '11', '2');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2023-05-26', '1', '1', '2');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2023-05-26', '2', '1', '2');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2023-05-26', '3', '1', '2');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2023-05-26', '4', '1', '2');

INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-18', '3', '10', '2');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-18', '1', '10', '2');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-18', '2', '10', '2');
INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES ('2024-05-18', '4', '10', '2');
