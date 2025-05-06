INSERT INTO RESERVATION_TIME (START_AT)
VALUES
    ('10:00'),
    ('12:00'),
    ('14:00'),
    ('16:00'),
    ('18:00'),
    ('20:00');

INSERT INTO THEME (NAME, DESCRIPTION, THUMBNAIL)
VALUES
    ('레벨1 탈출', '우테코 레벨1을 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
    ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
    ('레벨3 탈출', '우테코 레벨3을 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
    ('레벨4 탈출', '우테코 레벨4를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
    ('레벨5 탈출', '우테코 레벨5를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO RESERVATION (NAME, DATE, TIME_ID, THEME_ID)
VALUES
    ('예약1', '2025-05-04', 1, 2),
    ('예약2', '2025-05-04', 2, 2),
    ('예약3', '2025-05-04', 3, 2),
    ('예약4', '2025-05-04', 4, 1),
    ('예약5', '2025-05-04', 5, 1),
    ('예약6', '2025-05-03', 3, 3),
    ('예약7', '2025-05-03', 4, 4),
    ('예약8', '2025-05-03', 5, 5);
