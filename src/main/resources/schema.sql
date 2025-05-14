CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'MEMBER',
    PRIMARY KEY (id)
);

CREATE TABLE reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    date      VARCHAR(255) NOT NULL,
    member_id BIGINT,
    time_id   BIGINT,
    theme_id  BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);
