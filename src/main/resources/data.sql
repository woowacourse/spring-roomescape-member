INSERT INTO member
values (1000, 'admin', 'e@mail.com', 'password', 'admin'),
       (2000, '아토', 'atto@mail.com', 'attoword', 'user'),
       (3000, '안돌', 'andole@mail.com', 'andoleword', 'user');

INSERT INTO reservation_time
values (1000, '12:00'),
       (2000, '16:00');

INSERT INTO theme
values (1000, '테마1', '테마1입니다', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (2000, '테마2', '테마2입니다', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       (3000, '테마3', '테마3입니다', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation
values (1000, 1000, '2024-05-01', 1000, 2000),
       (2000, 2000, '2024-05-01', 2000, 2000),
       (3000, 3000, '2024-04-30', 1000, 2000),
       (4000, 1000, '2024-05-01', 1000, 1000),
       (5000, 2000, '2024-05-01', 2000, 1000),
       (6000, 3000, '2024-05-01', 1000, 3000);
