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

INSERT INTO member (name, email, password, role)
VALUES
    ('Admin', 'admin@gmail.com', 'password', 'ADMIN'),
    ('Alice', 'alice@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Bob', 'bob@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Carol', 'carol@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Dave', 'dave@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Eve', 'eve@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Frank', 'frank@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Grace', 'grace@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Heidi', 'heidi@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Ivan', 'ivan@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Judy', 'judy@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Mallory', 'mallory@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Niaj', 'niaj@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Olivia', 'olivia@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Peggy', 'peggy@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Rupert', 'rupert@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Sybil', 'sybil@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Trent', 'trent@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Uma', 'uma@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Victor', 'victor@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Wendy', 'wendy@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Xander', 'xander@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Yvonne', 'yvonne@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Zack', 'zack@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Amy', 'amy@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Brian', 'brian@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Chloe', 'chloe@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER'),
    ('Daniel', 'daniel@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqF.W', 'USER');

INSERT INTO reservation(date, time_id, theme_id, member_id)
VALUES
    -- theme_id = 12 (6회)
    ('2025-05-05', 1, 12, 1), -- Alice
    ('2025-05-05', 2, 12, 2), -- Bob
    ('2025-05-05', 3, 12, 3), -- Carol
    ('2025-05-05', 4, 12, 4), -- Dave
    ('2025-05-05', 5, 12, 5), -- Eve
    ('2025-05-05', 1, 12, 6), -- Frank

    -- theme_id = 11 (5회)
    ('2025-05-05', 2, 11, 7), -- Grace
    ('2025-05-05', 3, 11, 8), -- Heidi
    ('2025-05-05', 4, 11, 9), -- Ivan
    ('2025-05-05', 5, 11, 10), -- Judy
    ('2025-05-05', 1, 11, 11), -- Mallory

    -- theme_id = 3 (4회)
    ('2025-05-05', 2, 3, 12), -- Niaj
    ('2025-05-05', 3, 3, 13), -- Olivia
    ('2025-05-05', 4, 3, 14), -- Peggy
    ('2025-05-05', 5, 3, 15), -- Rupert

    -- theme_id = 4 (3회)
    ('2025-05-05', 1, 4, 16), -- Sybil
    ('2025-05-05', 2, 4, 17), -- Trent
    ('2025-05-05', 3, 4, 18), -- Uma

    -- theme_id = 5 (2회)
    ('2025-05-05', 4, 5, 19), -- Victor
    ('2025-05-05', 5, 5, 20), -- Wendy

    -- theme_id = 6 (1회)
    ('2025-05-05', 1, 6, 21), -- Xander

    -- theme_id = 7 (1회)
    ('2025-05-05', 2, 7, 22), -- Yvonne

    -- theme_id = 8 (1회)
    ('2025-05-05', 3, 8, 23), -- Zack

    -- theme_id = 9 (1회)
    ('2025-05-05', 4, 9, 24), -- Amy

    -- theme_id = 10 (1회)
    ('2025-05-05', 5, 10, 25), -- Brian

    -- theme_id = 11 (1회)
    ('2025-05-05', 1, 11, 26), -- Chloe

    -- theme_id = 12 (1회)
    ('2025-05-05', 2, 12, 27); -- Daniel
