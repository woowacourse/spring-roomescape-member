CREATE TABLE IF NOT EXISTS user
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(10)  NOT NULL,
    email    VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(20)  NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL UNIQUE,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS reservation
(
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    name             VARCHAR(10) NOT NULL,
    reservation_date DATE        NOT NULL,
    time_id          BIGINT      NOT NULL,
    theme_id         BIGINT      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    CONSTRAINT reservation_date_time_theme UNIQUE (reservation_date, time_id, theme_id)
);

