DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;

CREATE TABLE reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     VARCHAR(255) NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

INSERT INTO reservation_time (start_at)
VALUES ('09:00'),
       ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00'),
       ('14:00'),
       ('15:00'),
       ('16:00'),
       ('17:00'),
       ('18:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('정글 모험', '열대 정글의 심연을 탐험하세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('우주 오디세이', '별들을 넘어 우주 여행을 떠나세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('보물 찾기', '잃어버린 보물을 찾아 모험을 떠나세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('미스터리 저택', '저택 안에 숨겨진 미스터리를 풀어보세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('해적의 만', '바다를 항해하며 묻힌 금을 찾아보세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('고대 이집트의 미스터리', '고대 이집트의 파라오 무덤을 탐험하며 수수께끼를 풀어보세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('시간 여행자', '시간을 넘나들며 역사적 사건을 체험하세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('좀비 아포칼립스', '좀비가 지배하는 세계에서 살아남으세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('비밀의 숲', '마법의 숲을 탐험하며 숨겨진 비밀을 발견하세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('아서 왕의 검', '아서 왕의 전설적인 검, 엑스칼리버를 찾아나서는 모험을 시작하세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('드래곤의 둥지', '드래곤이 지키는 보물을 찾아 신비로운 동굴을 탐험하세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('빙하 속의 비밀', '빙하에 갇힌 고대 생물의 비밀을 파헤치세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('사막의 오아시스', '끝없는 사막 속 오아시스에 숨겨진 수수께끼를 풀어보세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('불가사의한 서커스', '마법 같은 서커스에서 벌어지는 미스터리를 조사하세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('광산 속 보물', '포기된 광산 속 숨겨진 보물을 찾으세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES
    ('박지민', '2024-04-29', 10, 1),
    ('정민서', '2024-04-28', 10, 1),
    ('최윤아', '2024-04-28', 4, 1),
    ('최우진', '2024-04-29', 8, 2),
    ('한소희', '2024-04-30', 9, 2),
    ('정하은', '2024-04-29', 2, 3),
    ('한지원', '2024-05-02', 7, 3),
    ('박병우', '2024-05-03', 3, 4),
    ('이하늬', '2024-05-02', 8, 5),
    ('이상호', '2024-04-27', 2, 5),
    ('이종수', '2024-05-01', 4, 6),
    ('김서연', '2024-05-02', 10, 6),
    ('김태리', '2024-05-01', 6, 7),
    ('김철수', '2024-04-27', 3, 7),
    ('최지수', '2024-05-03', 6, 8),
    ('정은지', '2024-04-30', 3, 8),
    ('정태영', '2024-05-01', 5, 9),
    ('김영희', '2024-04-30', 7, 9),
    ('이민준', '2024-05-03', 5, 10),
    ('박찬호', '2024-05-02', 2, 11),
    ('한민수', '2024-05-01', 1, 13),
    ('정수민', '2024-04-27', 8, 13),
    ('김하늘', '2024-05-03', 4, 14),
    ('한가인', '2024-04-28', 9, 14),
    ('박소연', '2024-04-28', 6, 14),
    ('한지훈', '2024-04-29', 5, 15);
