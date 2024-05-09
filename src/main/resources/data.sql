INSERT INTO reservation_time(start_at) VALUES ('10:30'),
                                    ('11:30'),
                                    ('12:30'),
                                    ('13:30'),
                                    ('14:30'),
                                    ('15:30');

INSERT INTO theme(name, description, thumbnail) VALUES ('테마 1', '테마 1입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 2', '테마 2입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 3', '테마 3입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 4', '테마 4입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 5', '테마 5입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 6', '테마 6입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 7', '테마 7입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 8', '테마 8입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ('테마 9', '테마 9입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ( '테마 10', '테마 10입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                         ( '테마 11', '테마 11입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('잉크', '2024-04-25', 1, 1),
                               ('잉크', '2024-04-25', 2, 1),
                               ('잉크', '2024-04-26', 3, 2),
                               ('뽀로로', '2024-04-26', 4, 2),
                               ('뽀로로', '2024-04-27', 5, 3),
                               ('뽀로로', '2024-04-28', 6, 2),
                               ('뽀로로', '2024-04-29', 1, 2);

INSERT INTO member(name, email, password) VALUES ('잉크', 'asdf@a.com', '1234');
