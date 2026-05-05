CREATE TABLE theme
(
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    name                VARCHAR(20)  NOT NULL,
    description         VARCHAR(255) NOT NULL,
    thumbnail_image_url VARCHAR(500) NOT NULL,
    is_active           TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_theme_name_is_active UNIQUE (name, is_active)
);

CREATE TABLE reservation_time (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at TIME         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_reservation_time_start_at UNIQUE (start_at)
);

CREATE TABLE reservation (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(20) NOT NULL,
    date    DATE         NOT NULL,
    theme_id BIGINT,
    time_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    CONSTRAINT uk_reservation_date_time UNIQUE (date, time_id)
);
