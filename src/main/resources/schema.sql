drop table if exists member CASCADE;
drop table if exists theme CASCADE;
drop table if exists reservation CASCADE;
drop table if exists reservation_time CASCADE;

CREATE TABLE member
(
    id       BIGINT        NOT NULL AUTO_INCREMENT,
    role     VARCHAR(30)   NOT NULL,
    password VARCHAR(5000) NOT NULL,
    name     VARCHAR(20)   NOT NULL,
    email    VARCHAR(30)   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)  NOT NULL,
    description VARCHAR(500)  NOT NULL,
    thumbnail   VARCHAR(1000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id       BIGINT     NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    date     DATE       NOT NULL,
    time_id  BIGINT, -- 컬럼 수정
    theme_id BIGINT, -- 컬럼 수정
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);
