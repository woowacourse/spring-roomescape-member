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


INSERT INTO RESERVATION(name, date, time_id, theme_id)
VALUES -- 테마 1-10 (각 2개씩)
       ('재훈재훈', DATEADD('DAY', -25, CURRENT_DATE), 1, 1),
       ('이영희', DATEADD('DAY', -24, CURRENT_DATE), 2, 1),
       ('박민준', DATEADD('DAY', -23, CURRENT_DATE), 3, 2),
       ('정수빈', DATEADD('DAY', -22, CURRENT_DATE), 1, 2),
       ('홍길동', DATEADD('DAY', -21, CURRENT_DATE), 2, 3),
       ('최지민', DATEADD('DAY', -20, CURRENT_DATE), 3, 3),
       ('강동원', DATEADD('DAY', -19, CURRENT_DATE), 1, 4),
       ('윤서연', DATEADD('DAY', -18, CURRENT_DATE), 2, 4),
       ('남태현', DATEADD('DAY', -17, CURRENT_DATE), 3, 5),
       ('서지수', DATEADD('DAY', -16, CURRENT_DATE), 1, 5),
       ('임현우', DATEADD('DAY', -15, CURRENT_DATE), 2, 6),
       ('한소희', DATEADD('DAY', -14, CURRENT_DATE), 3, 6),
       ('오민석', DATEADD('DAY', -13, CURRENT_DATE), 1, 7),
       ('신지원', DATEADD('DAY', -12, CURRENT_DATE), 2, 7),
       ('배준호', DATEADD('DAY', -11, CURRENT_DATE), 3, 8),
       ('조은서', DATEADD('DAY', -10, CURRENT_DATE), 1, 8),
       ('장현수', DATEADD('DAY', -9, CURRENT_DATE), 2, 9),
       ('권미나', DATEADD('DAY', -8, CURRENT_DATE), 3, 9),
       ('문상철', DATEADD('DAY', -7, CURRENT_DATE), 1, 10),
       ('송하은', DATEADD('DAY', -6, CURRENT_DATE), 2, 10),

       -- 테마 11-15 (각 1개씩)
       ('동준초이', DATEADD('DAY', -5, CURRENT_DATE), 3, 11),
       ('류지아', DATEADD('DAY', -4, CURRENT_DATE), 1, 12),
       ('구민재', DATEADD('DAY', -3, CURRENT_DATE), 2, 13),
       ('허연우', DATEADD('DAY', -2, CURRENT_DATE), 3, 14),
       ('이서진', DATEADD('DAY', -1, CURRENT_DATE), 1, 15)
;
