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
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE member
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT,
    theme_id BIGINT,
    member_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- INSERT INTO member (name, email, password)
-- VALUES ('어드민', 'admin@email.com', 'password');

-- 테스트용
INSERT INTO reservation_time (start_at)
VALUES ('09:00');

INSERT INTO reservation_time (start_at)
VALUES ('10:00');

INSERT INTO reservation_time (start_at)
VALUES ('11:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨1 탈출', '우테코 레벨1를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨3 탈출', '우테코 레벨3를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO member (name, email, password)
VALUES ('카고', 'kargo123@email.com', '123456');

INSERT INTO member (name, email, password)
VALUES ('브라운', 'brown123@email.com', '123456');

INSERT INTO member (name, email, password)
VALUES ('솔라', 'solar123@email.com', '123456');

INSERT INTO member (name, email, password)
VALUES ('어드민', 'admin@email.com', 'password');

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-25', 1, 1, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2099-05-01', 1, 1, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-25', 2, 1, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-25', 2, 2, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-27', 1, 2, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-27', 2, 2, 1);
