INSERT INTO users(name, email, password, role)
VALUES ('name1', 'email1@email.com', '1234', 'USER'),
       ('name2', 'email2@email.com', '1234', 'USER'),
       ('name3', 'email3@email.com', '1234', 'ADMIN')
;

INSERT INTO RESERVATION_TIME(start_at)
VALUES ('13:40'),
       ('14:40'),
       ('15:40'),
       ('23:58')
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


INSERT INTO RESERVATION(date, user_id, time_id, theme_id)
VALUES -- 테마 1-10 (각 2개씩)
       ('2023-03-01', 1, 1, 1),
       ('2023-03-02', 1, 2, 1),
       ('2023-03-03', 1, 3, 2),
       ('2023-03-04', 1, 1, 2),
       ('2023-03-05', 1, 2, 3),
       ('2023-03-06', 2, 3, 3),
       ('2023-03-07', 2, 1, 4),
       ('2023-03-08', 2, 2, 4),
       ('2023-03-09', 2, 3, 5),
       ('2023-03-10', 2, 1, 5),
       ('2023-03-11', 2, 2, 6),
       ('2023-03-12', 2, 3, 6),
       ('2023-03-13', 2, 1, 7),
       ('2023-03-14', 2, 2, 7),
       ('2023-03-15', 2, 3, 8),
       ('2023-03-16', 2, 1, 8),
       ('2023-03-17', 2, 2, 9),
       ('2023-03-18', 2, 3, 9),
       ('2023-03-19', 2, 1, 10),
       ('2023-03-20', 2, 2, 10),

       -- 테마 11-15 (각 1개씩)
       ('2023-03-21', 1, 3, 11),
       ('2023-03-22', 1, 1, 12),
       ('2023-03-23', 1, 2, 13),
       ('2023-03-24', 1, 3, 14),
       ('2023-03-25', 1, 1, 15)
;

