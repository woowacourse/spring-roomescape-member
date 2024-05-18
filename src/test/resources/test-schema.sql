DROP TABLE IF EXISTS reservation_time CASCADE;
DROP TABLE IF EXISTS theme CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (start_at)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE reservation
(
    id        BIGINT NOT NULL AUTO_INCREMENT,
    date      DATE   NOT NULL,
    time_id   BIGINT,
    theme_id  BIGINT,
    member_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    UNIQUE (date, time_id, theme_id, member_id)
);
