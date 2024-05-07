CREATE TABLE reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL UNIQUE,
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
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    name                VARCHAR(255) NOT NULL,
    date                DATE         NOT NULL,
    reservation_time_id BIGINT       NOT NULL,
    theme_id            BIGINT       NOT NULL,
    UNIQUE (date, reservation_time_id, theme_id),-- 컬럼 추가
    PRIMARY KEY (id),
    FOREIGN KEY (reservation_time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id) -- 외래키 추가
);
