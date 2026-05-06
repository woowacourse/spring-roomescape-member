CREATE TABLE reservation_time (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation_theme (
    id          BIGINT           NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255)     NOT NULL,
    description VARCHAR(255)     NOT NULL,
    image_url   VARCHAR(255)     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation (
     id      BIGINT       NOT NULL AUTO_INCREMENT,
     name    VARCHAR(255) NOT NULL,
     date    VARCHAR(255) NOT NULL,
     time_id BIGINT NOT NULL,
     theme_id BIGINT NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     PRIMARY KEY (id),
     FOREIGN KEY (time_id) REFERENCES reservation_time (id),
     FOREIGN KEY (theme_id) REFERENCES reservation_theme (id)
);
