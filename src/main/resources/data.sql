INSERT INTO USERS(name, email, password, roles)
VALUES ('어드민1', 'admin1@test.com', 'test', 'NORMAL, ADMIN'),
       ('어드민2', 'admin2@test.com', 'test', 'NORMAL, ADMIN'),
       ('사용자1', 'user1@test.com', 'test', 'NORMAL'),
       ('사용자2', 'user2@test.com', 'test', 'NORMAL'),
       ('사용자3', 'user3@test.com', 'test', 'NORMAL'),
       ('사용자4', 'user4@test.com', 'test', 'NORMAL');

INSERT INTO RESERVATION_TIME(start_at)
VALUES ('13:40'),
       ('14:40'),
       ('15:40'),
       ('23:58'),
       ('01:40'),
       ('02:40'),
       ('03:40'),
       ('04:58'),
       ('05:40'),
       ('06:40'),

       ('07:40'),
       ('08:58')
;

INSERT INTO THEME(name, description, thumbnail)
VALUES ('첫번째', '일번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('두번째', '이번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('세번째', '삼번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('네번째', '사번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('다섯번째', '오번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('여섯번째', '육번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('일곱번째', '칠번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('여덟번째', '팔번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('아홉번째', '구번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('열번째', '십번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('열한번째', '십일번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('열두번째', '십이번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('열세번째', '십삼번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('열네번째', '십사번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('열다섯번째', '십오번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('열여섯번째', '십육번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg')
;


INSERT INTO RESERVATION(user_id, date, time_id, theme_id)
VALUES -- 테마 1-10 (각 2개씩)
       (3, DATEADD('DAY', -25, CURRENT_DATE), 1, 1),
       (4, DATEADD('DAY', -24, CURRENT_DATE), 2, 1),
       (5, DATEADD('DAY', -23, CURRENT_DATE), 3, 2),
       (6, DATEADD('DAY', -22, CURRENT_DATE), 1, 2),
       (3, DATEADD('DAY', -21, CURRENT_DATE), 2, 3),
       (4, DATEADD('DAY', -20, CURRENT_DATE), 3, 3),
       (5, DATEADD('DAY', -19, CURRENT_DATE), 1, 4),
       (6, DATEADD('DAY', -18, CURRENT_DATE), 2, 4),
       (3, DATEADD('DAY', -17, CURRENT_DATE), 3, 5),
       (4, DATEADD('DAY', -16, CURRENT_DATE), 1, 5),

       (5, DATEADD('DAY', -15, CURRENT_DATE), 2, 6),
       (6, DATEADD('DAY', -14, CURRENT_DATE), 3, 6),
       (3, DATEADD('DAY', -13, CURRENT_DATE), 1, 7),
       (4, DATEADD('DAY', -12, CURRENT_DATE), 2, 7),
       (5, DATEADD('DAY', -11, CURRENT_DATE), 3, 8),
       (6, DATEADD('DAY', -10, CURRENT_DATE), 1, 8),
       (3, DATEADD('DAY', -9, CURRENT_DATE), 2, 9),
       (4, DATEADD('DAY', -8, CURRENT_DATE), 3, 9),
       (5, DATEADD('DAY', -7, CURRENT_DATE), 1, 10),
       (3, DATEADD('DAY', -6, CURRENT_DATE), 2, 10),

       -- 테마 11-15 (각 1개씩)
       (3, DATEADD('DAY', -5, CURRENT_DATE), 3, 11),
       (6, DATEADD('DAY', -4, CURRENT_DATE), 1, 12),
       (3, DATEADD('DAY', -3, CURRENT_DATE), 2, 13),
       (6, DATEADD('DAY', -2, CURRENT_DATE), 3, 14),
       (4, DATEADD('DAY', -1, CURRENT_DATE), 1, 15)
;
