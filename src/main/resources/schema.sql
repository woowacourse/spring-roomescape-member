CREATE TABLE reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at TIME NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL UNIQUE ,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    time_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

CREATE INDEX idx_reservation_theme_time_date
ON reservation (theme_id, time_id, date);

