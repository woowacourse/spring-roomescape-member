CREATE TABLE reservation_time (
    id         BIGINT    NOT NULL AUTO_INCREMENT,
    start_at   TIME      NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE theme (
    id                 BIGINT           NOT NULL AUTO_INCREMENT,
    name               VARCHAR(255)     NOT NULL UNIQUE,
    description        VARCHAR(255)     NOT NULL,
    thumbnail_url      VARCHAR(1024)    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation (
    id                 BIGINT          NOT NULL AUTO_INCREMENT,
    name               VARCHAR(50)     NOT NULL,
    reservation_date   DATE            NOT NULL,
    time_id            BIGINT          NOT NULL,
    theme_id           BIGINT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id)  REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    UNIQUE (theme_id, reservation_date, time_id)
);

CREATE INDEX idx_reservation_date_theme ON reservation (reservation_date, theme_id);
