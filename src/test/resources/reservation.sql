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

INSERT INTO reservation(id, name, reservation_date, time_id, theme_id)
VALUES (2, '프린', '2024-05-01', 1, 1),
       (3, '프린', '2024-05-01', 2, 1),
       (4, '프린', '2024-05-01', 3, 1),
       (5, '프린', '2024-05-01', 4, 1),
       (6, '레모네', '2024-05-01', 5, 1),
       (7, '레모네', '2024-05-01', 1, 2),
       (8, '레모네', '2024-05-01', 2, 2),
       (9, '레모네', '2024-05-01', 1, 3),
       (10, '오리', '2024-05-01', 2, 3),
       (11, '오리', '2024-05-01', 3, 3);
