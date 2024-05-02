DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;

CREATE TABLE reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
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

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO theme (name, description, thumbnail)
VALUES ('정글 모험', '열대 정글의 심연을 탐험하세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('우주 오디세이', '별들을 넘어 우주 여행을 떠나세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('보물 찾기', '잃어버린 보물을 찾아 모험을 떠나세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('미스터리 저택', '저택 안에 숨겨진 미스터리를 풀어보세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('해적의 만', '바다를 항해하며 묻힌 금을 찾아보세요.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

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

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('John Doe', '2024-04-23', 1, 1),
       ('Jane Smith', '2024-04-24', 2, 2),
       ('Alice Johnson', '2024-04-25', 3, 1),
       ('Bob Brown', '2024-04-26', 4, 3),
       ('Charlie Davis', '2024-04-27', 5, 1),
       ('Diana Adams', '2024-04-28', 6, 2),
       ('Evan Wright', '2024-04-29', 7, 2);