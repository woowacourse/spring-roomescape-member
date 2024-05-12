INSERT INTO member(id, name, email, password, role)
VALUES (1, '프린', 'prin@gmail.com', '1q2w3e4r!@', 'ADMIN'),
       (2, '레모네', 'remone@gmail.com', '1q2w3e4r!@', 'MEMBER'),
       (3, '오리', 'duck@gmail.com', '1q2w3e4r!@', 'MEMBER');

INSERT INTO reservation_time(id, start_at)
VALUES (1, '01:00'),
       (2, '02:00'),
       (3, '03:00'),
       (4, '04:00'),
       (5, '05:00');

INSERT INTO theme(id, name, description, thumbnail)
VALUES (1, '셜록홈즈', '설명1', 'https://lemone1.com'),
       (2, '해리포터', '설명2', 'https://lemone2.com'),
       (3, '이순신', '설명3', 'https://lemone3.com');

INSERT INTO reservation(id, reservation_date, time_id, theme_id, member_id)
VALUES (2, '2024-04-27', 1, 1, 1),
       (3, '2024-04-28', 2, 1, 1),
       (4, '2024-04-29', 3, 1, 1),
       (5, '2024-04-30', 4, 1, 1),
       (6, '2024-05-01', 5, 1, 1),
       (7, '2024-04-26', 1, 2, 1),
       (8, '2024-04-27', 2, 2, 1),
       (9, '2024-04-30', 1, 3, 1),
       (10, '2024-05-01', 2, 3, 1),
       (11, '2024-05-01', 3, 3, 1);
