DROP TABLE IF EXISTS reservation_history;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;

CREATE TABLE theme
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(255),
    description   VARCHAR(500),
    status   VARCHAR(20) CHECK ( status IN ('AVAILABLE', 'DELETED')),
    PRIMARY KEY (id),
    UNIQUE (name)
);


CREATE TABLE reservation_time
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    start_at TIME        NOT NULL,
    status   VARCHAR(20) CHECK ( status IN ('AVAILABLE', 'DELETED')),
    PRIMARY KEY (id),
    UNIQUE (start_at)
);


CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    PRIMARY KEY (id),

    UNIQUE (date, time_id, theme_id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);


CREATE TABLE reservation_history
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    name         VARCHAR(255) NOT NULL,
    date         DATE         NOT NULL,
    start_at     TIME         NOT NULL,
    theme_name   VARCHAR(255) NOT NULL,
    cancelled_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
