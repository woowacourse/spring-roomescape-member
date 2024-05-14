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

CREATE TABLE member
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    varchar(255) NOT NULL,
    role        varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    date     VARCHAR(255) NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    member_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
