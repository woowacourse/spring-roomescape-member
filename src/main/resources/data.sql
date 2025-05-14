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
VALUES ('10:00'),
       ('12:00'),
       ('14:00'),
       ('16:00'),
       ('18:00');

INSERT INTO reservation(member_id, date, time_id, theme_id)
VALUES (1, '2025-05-07', 1, 1),
       (1, '2025-05-07', 1, 1),
       (1, '2025-05-07', 1, 2),
       (1, '2025-05-07', 1, 4),
       (1, '2025-05-07', 1, 6),
       (1, '2025-05-07', 1, 11),

       (1, '2025-05-07', 2, 1),
       (1, '2025-05-07', 2, 2),
       (1, '2025-05-07', 2, 4),
       (1, '2025-05-07', 2, 7),
       (1, '2025-05-07', 2, 12),

       (1, '2025-05-07', 3, 1),
       (1, '2025-05-07', 3, 2),
       (1, '2025-05-07', 3, 3),
       (1, '2025-05-07', 3, 4),
       (1, '2025-05-07', 3, 8),

       (1, '2025-05-07', 4, 1),
       (1, '2025-05-07', 4, 2),
       (1, '2025-05-07', 4, 3),
       (1, '2025-05-07', 4, 5),
       (1, '2025-05-07', 4, 9),

       (1, '2025-05-07', 5, 1),
       (1, '2025-05-07', 5, 2),
       (1, '2025-05-07', 5, 3),
       (1, '2025-05-07', 5, 5),
       (1, '2025-05-07', 5, 10);

INSERT INTO member (name, email, password, role)
VALUES ('Danny', 'danny@example.com', '0000', 'ADMIN'),
       ('Mint', 'mint@example.com', '0000', 'MEMBER');


