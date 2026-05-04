CREATE TABLE reservation_time (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    date    VARCHAR(255) NOT NULL,
    theme_id BIGINT NOT NULL,
    time_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
);

CREATE TABLE theme (
    id      BIGINT      NOT NULL AUTO_INCREMENT,
    name    VARCHAR(20) NOT NULL,
    description TEXT,
    url     VARCHAR(255) NOT NULL
    PRIMARY KEY (id)
);
