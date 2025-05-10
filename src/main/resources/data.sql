INSERT INTO MEMBER (NAME, EMAIL, PASSWORD, ROLE)
values
    ('이름1', 'ex1@email.com', 'pw1', 'ADMIN'),
    ('이름2', 'ex2@email.com', 'pw2', 'USER'),
    ('이름3', 'ex3@email.com', 'pw3', 'USER');

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
--
INSERT INTO RESERVATION (MEMBER_ID, DATE, TIME_ID, THEME_ID)
VALUES
    (1, '2025-05-04', 1, 2),
    (2, '2025-05-04', 2, 2),
    (3, '2025-05-04', 3, 2),
    (1, '2025-05-04', 4, 1),
    (2, '2025-05-04', 5, 1),
    (3, '2025-05-03', 3, 3),
    (1, '2025-05-03', 4, 4),
    (2, '2025-05-03', 5, 5);
