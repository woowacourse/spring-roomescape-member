DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS times;
DROP TABLE IF EXISTS themes;

CREATE TABLE times
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (start_at)
);

CREATE TABLE themes
(
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    name          VARCHAR(40) NOT NULL,
    thumbnail_url VARCHAR(2048),
    description   VARCHAR(400),
    PRIMARY KEY (id),
    UNIQUE (name)
);


CREATE TABLE reservations
(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    name       VARCHAR(20) NOT NULL,
    date       DATE        NOT NULL,
    time_id    BIGINT,
    theme_id   BIGINT,
    status     VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
    deleted_at TIMESTAMP   NULL     DEFAULT NULL,
    version    BIGINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES times (id),
    FOREIGN KEY (theme_id) REFERENCES themes (id),
    UNIQUE (theme_id, date, time_id, deleted_at)
);



