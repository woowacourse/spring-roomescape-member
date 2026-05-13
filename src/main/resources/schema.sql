CREATE TABLE theme
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(250) NOT NULL,
    description   VARCHAR(250) NOT NULL,
    thumbnail_url VARCHAR(250) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    status   VARCHAR(255) NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

CREATE TABLE theme_slot
(
    id          BIGINT  NOT NULL AUTO_INCREMENT,
    theme_id    BIGINT  NOT NULL,
    date        DATE    NOT NULL,
    time_id     BIGINT  NOT NULL,
    is_reserved BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    UNIQUE (theme_id, date, time_id)
);
