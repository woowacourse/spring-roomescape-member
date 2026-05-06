CREATE TABLE reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation_date
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    date VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    url     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date_id  BIGINT,
    time_id  BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (date_id) REFERENCES reservation_date (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00'),
       (2, '11:00'),
       (3, '13:00'),
       (4, '15:00');

INSERT INTO reservation_date (id, date)
VALUES (1, '2026-05-01'),
       (2, '2026-05-02'),
       (3, '2026-05-03'),
       (4, '2026-05-04'),
       (5, '2026-05-05'),
       (6, '2026-05-06'),
       (7, '2026-05-07'),
       (8, '2026-05-08');

INSERT INTO theme (id, name, content, url)
VALUES (1, '공포', '오금이 저리는 공포입니다.', '/themes/scary'),
       (2, '스릴러', '액션이 가미된 스릴러입니다.', '/themes/thriller'),
       (3, '청춘물', '학교 배경인 테마 입니다.', '/themes/youth'),
       (4, '미스터리', '단서를 따라 진실을 밝히는 추리 테마입니다.', '/themes/mystery'),
       (5, '판타지', '마법과 전설이 살아있는 판타지 테마입니다.', '/themes/fantasy'),
       (6, '우주', '우주정거장을 배경으로 한 SF 테마입니다.', '/themes/space'),
       (7, '잠입', '금고를 털기 위한 잠입 작전 테마입니다.', '/themes/infiltration'),
       (8, '재난', '제한 시간 안에 탈출해야 하는 재난 테마입니다.', '/themes/disaster'),
       (9, '사극', '왕실의 비밀을 쫓는 사극 테마입니다.', '/themes/history'),
       (10, '모험', '유적을 탐험하는 어드벤처 테마입니다.', '/themes/adventure'),
       (11, '코미디', '유쾌한 소동이 가득한 코미디 테마입니다.', '/themes/comedy'),
       (12, '느와르', '어두운 도시를 배경으로 한 느와르 테마입니다.', '/themes/noir');

INSERT INTO reservation (id, name, date_id, time_id, theme_id)
VALUES (1, '보예', 1, 1, 1),
       (2, '이산', 1, 2, 1),
       (3, '나무', 2, 1, 2),
       (4, '피즈', 2, 3, 2),
       (5, '제이콥', 3, 1, 1),
       (6, '보예짱', 3, 4, 3),
       (7, '이산짱', 4, 2, 1),
       (8, '나무짱', 4, 3, 3),
       (9, '피즈짱', 5, 1, 2),
       (10, '샤를', 6, 4, 1),
       (11, '마이찬', 7, 2, 1),
       (12, '샤를짱', 1, 3, 8),
       (13, '마이찬짱', 1, 4, 5),
       (14, '브라운', 2, 2, 11),
       (15, '네오', 2, 4, 4),
       (16, '브리', 3, 2, 9),
       (17, '구구', 3, 3, 6),
       (18, '리사', 4, 1, 12),
       (19, '레서', 4, 4, 7),
       (20, '바니', 5, 2, 10),
       (21, '소낙눈', 5, 3, 3),
       (22, '카야', 5, 4, 8),
       (23, '피노', 6, 1, 5),
       (24, '우디', 6, 2, 11),
       (25, '캐모', 6, 3, 4),
       (26, '아이큐', 7, 1, 9),
       (27, '쿠다', 7, 3, 6),
       (28, '고래', 7, 4, 10);
