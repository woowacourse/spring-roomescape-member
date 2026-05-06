CREATE TABLE reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE reservation_date
(
    id   BIGINT NOT NULL AUTO_INCREMENT,
    date DATE   NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(255) NOT NULL,
    is_active     BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    time     TIME         NOT NULL,
    theme_id BIGINT       NOT NULL,
    status   ENUM('RESERVED', 'CANCELED') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (date) REFERENCES reservation_date (date),
    FOREIGN KEY (time) REFERENCES reservation_time (start_at),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);
