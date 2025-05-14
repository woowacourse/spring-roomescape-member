CREATE TABLE member
(
    member_id BIGINT                  NOT NULL AUTO_INCREMENT,
    name      VARCHAR(255)            NOT NULL,
    email     VARCHAR(255)            NOT NULL UNIQUE,
    password  VARCHAR(255)            NOT NULL,
    role      ENUM('ADMIN', 'MEMBER') NOT NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE reservation_time
(
    time_id                   BIGINT NOT NULL AUTO_INCREMENT,
    start_at                  TIME   NOT NULL,
    PRIMARY KEY (time_id)
);

CREATE TABLE theme
(
    theme_id    BIGINT       NOT NULL AUTO_INCREMENT,
    theme_name  VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (theme_id)
);

CREATE TABLE reservation
(
    reservation_id       BIGINT       NOT NULL AUTO_INCREMENT,
    member_id            BIGINT       NOT NULL,
    date                 DATE         NOT NULL,
    time_id              BIGINT       NOT NULL,
    theme_id             BIGINT       NOT NULL,
    PRIMARY KEY (reservation_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (time_id),
    FOREIGN KEY (theme_id) REFERENCES theme (theme_id)
);
