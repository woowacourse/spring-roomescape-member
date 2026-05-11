-- schema.sql
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;

CREATE TABLE IF NOT EXISTS reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reservation
(
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL,
    theme_id  BIGINT       NOT NULL,
    date      DATE         NOT NULL,
    time_id   BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    UNIQUE (date, theme_id, time_id)
);
