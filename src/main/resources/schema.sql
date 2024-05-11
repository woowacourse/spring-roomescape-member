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

CREATE TABLE member
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    name     VARCHAR(5)  NOT NULL,
    email    VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    date      VARCHAR(255) NOT NULL,
    member_id BIGINT       NOT NULL,                -- 컬럼 추가
    time_id   BIGINT       NOT NULL,
    theme_id  BIGINT       NOT NULL,                -- 컬럼 추가
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id), -- 외래키 추가
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)    -- 외래키 추가
);
