INSERT INTO RESERVATION_TIME(start_at)
VALUES ('13:40'),
       ('14:40'),
       ('15:40')
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
       ('열다섯번째', '십오번방', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg')
;


INSERT INTO RESERVATION(name, date, time_id, theme_id)
VALUES -- 테마 1-10 (각 2개씩)
       ('재훈재훈', '2023-03-01', 1, 1),
       ('이영희', '2023-03-02', 2, 1),
       ('박민준', '2023-03-03', 3, 2),
       ('정수빈', '2023-03-04', 1, 2),
       ('홍길동', '2023-03-05', 2, 3),
       ('최지민', '2023-03-06', 3, 3),
       ('강동원', '2023-03-07', 1, 4),
       ('윤서연', '2023-03-08', 2, 4),
       ('남태현', '2023-03-09', 3, 5),
       ('서지수', '2023-03-10', 1, 5),
       ('임현우', '2023-03-11', 2, 6),
       ('한소희', '2023-03-12', 3, 6),
       ('오민석', '2023-03-13', 1, 7),
       ('신지원', '2023-03-14', 2, 7),
       ('배준호', '2023-03-15', 3, 8),
       ('조은서', '2023-03-16', 1, 8),
       ('장현수', '2023-03-17', 2, 9),
       ('권미나', '2023-03-18', 3, 9),
       ('문상철', '2023-03-19', 1, 10),
       ('송하은', '2023-03-20', 2, 10),

       -- 테마 11-15 (각 1개씩)
       ('동준초이', '2023-03-21', 3, 11),
       ('류지아', '2023-03-22', 1, 12),
       ('구민재', '2023-03-23', 2, 13),
       ('허연우', '2023-03-24', 3, 14),
       ('이서진', '2023-03-25', 1, 15)
;
