DROP TABLE IF EXISTS RESERVATION;
DROP TABLE IF EXISTS THEME;
DROP TABLE IF EXISTS RESERVATION_TIME;

CREATE TABLE IF NOT EXISTS reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at TIME NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(10) NOT NULL,
    description VARCHAR(50) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(5) NOT NULL,
    date DATE NOT NULL,
    time_id BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);
