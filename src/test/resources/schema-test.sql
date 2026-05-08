CREATE TABLE IF NOT EXISTS themes
(
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255)  NOT NULL UNIQUE,
    description VARCHAR(255)  NOT NULL,
    thumbnail   VARCHAR(2048) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at TIME NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE NOT NULL,
    time_id  BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id) ON DELETE CASCADE,
    FOREIGN KEY (theme_id) REFERENCES themes (id) ON DELETE CASCADE,
    CONSTRAINT unique_reservation UNIQUE (date, time_id, theme_id)

);

