INSERT INTO theme(name, description, thumbnail)
VALUES ('추리', '셜록 with Danny', 'image/thumbnail.png'),
       ('공포', '어둠 속의 비명', 'image/thumbnail.png'),
       ('모험', '잃어버린 도시', 'image/thumbnail.png'),
       ('SF', '우주 탈출 미션', 'image/thumbnail.png'),
       ('감성', '시간을 걷는 집', 'image/thumbnail.png'),
       ('판타지', '마법사의 유산', 'image/thumbnail.png'),
       ('역사', '고려 왕실의 비밀', 'image/thumbnail.png'),
       ('범죄', '은행 강도 사건', 'image/thumbnail.png'),
       ('스릴러', '잠입 작전', 'image/thumbnail.png'),
       ('코미디', '웃음 연구소', 'image/thumbnail.png'),
       ('로맨스', '잃어버린 편지', 'image/thumbnail.png'),
       ('논리', '퍼즐 마스터', 'image/thumbnail.png');

INSERT INTO reservation_time(start_at)
VALUES ('08:00'),
       ('12:00'),
       ('14:00'),
       ('16:00'),
       ('18:00');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES
    -- theme_id = 1 (6회)
    ('Alice', '2025-05-05', 1, 1),
    ('Bob', '2025-05-05', 2, 1),
    ('Carol', '2025-05-05', 3, 1),
    ('Dave', '2025-05-05', 4, 1),
    ('Eve', '2025-05-05', 5, 1),
    ('Frank', '2025-05-05', 1, 1),

    -- theme_id = 2 (5회)
    ('Grace', '2025-05-05', 2, 2),
    ('Heidi', '2025-05-05', 3, 2),
    ('Ivan', '2025-05-05', 4, 2),
    ('Judy', '2025-05-05', 5, 2),
    ('Mallory', '2025-05-05', 1, 2),

    -- theme_id = 3 (4회)
    ('Niaj', '2025-05-05', 2, 3),
    ('Olivia', '2025-05-05', 3, 3),
    ('Peggy', '2025-05-05', 4, 3),
    ('Rupert', '2025-05-05', 5, 3),

    -- theme_id = 4 (3회)
    ('Sybil', '2025-05-05', 1, 4),
    ('Trent', '2025-05-05', 2, 4),
    ('Uma', '2025-05-05', 3, 4),

    -- theme_id = 5 (2회)
    ('Victor', '2025-05-05', 4, 5),
    ('Wendy', '2025-05-05', 5, 5),

    -- theme_id = 6 (1회)
    ('Xander', '2025-05-05', 1, 6),

    -- theme_id = 7 (1회)
    ('Yvonne', '2025-05-05', 2, 7),

    -- theme_id = 8 (1회)
    ('Zack', '2025-05-05', 3, 8),

    -- theme_id = 9 (1회)
    ('Amy', '2025-05-05', 4, 9),

    -- theme_id = 10 (1회)
    ('Brian', '2025-05-05', 5, 10),

    -- theme_id = 11 (1회)
    ('Chloe', '2025-05-05', 1, 11),

    -- theme_id = 12 (1회)
    ('Daniel', '2025-05-05', 2, 12);
