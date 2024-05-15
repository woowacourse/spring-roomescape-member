INSERT INTO member (name, email, password, role) VALUES ('냥인', 'nyangin@email.com', '1234', 'ADMIN');
INSERT INTO member (name, email, password, role) VALUES ('미아', 'mia@email.com', '1234', 'MEMBER');

INSERT INTO reservation_time (start_at) VALUES ('18:00'), ('19:00');

INSERT INTO theme (name, description, thumbnail) VALUES ('호러1', '매우 무섭습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('호러2', '매우 무섭습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('호러3', '매우 무섭습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('호러4', '매우 무섭습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('호러5', '매우 무섭습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('추리2', '매우 어렵습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('추리3', '매우 어렵습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('추리4', '매우 어렵습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('추리5', '매우 어렵습니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
                                                        ('준비 중', '준비 중입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (2, '2034-05-08', 1, 2),
                                                                    (2, '2034-05-09', 1, 2),
                                                                    (2, '2034-05-10', 1, 2),
                                                                    (2, '2034-05-11', 1, 2),
                                                                    (2, '2034-05-12', 1, 2),
                                                                    (2, '2034-05-08', 1, 1),
                                                                    (2, '2034-05-09', 1, 1),
                                                                    (2, '2034-05-10', 1, 1),
                                                                    (2, '2034-05-11', 1, 1),
                                                                    (2, '2034-05-08', 1, 3),
                                                                    (2, '2034-05-08', 2, 3),
                                                                    (2, '2034-05-09', 2, 3),
                                                                    (2, '2034-05-08', 2, 4),
                                                                    (2, '2034-05-08', 1, 4),
                                                                    (2, '2034-05-09', 1, 4),
                                                                    (2, '2034-05-09', 1, 5),
                                                                    (2, '2034-05-09', 2, 5),
                                                                    (2, '2034-05-09', 1, 6),
                                                                    (2, '2034-05-09', 2, 6),
                                                                    (2, '2034-05-10', 1, 7),
                                                                    (2, '2034-05-10', 2, 7),
                                                                    (2, '2034-05-10', 1, 8),
                                                                    (2, '2034-05-10', 2, 8),
                                                                    (2, '2034-05-11', 1, 9),
                                                                    (2, '2034-05-11', 2, 9),
                                                                    (2, '2034-05-11', 1, 10),
                                                                    (2, '2034-05-11', 2, 10);
