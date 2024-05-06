drop table if exists theme CASCADE;
drop table if exists reservation CASCADE;
drop table if exists reservation_time CASCADE;

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(20)  NOT NULL,
    description VARCHAR(80)  NOT NULL,
    thumbnail   VARCHAR(700) NOT NULL,
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
    name     VARCHAR(5) NOT NULL,
    date     DATE       NOT NULL,
    time_id  BIGINT, -- 컬럼 수정
    theme_id BIGINT, -- 컬럼 수정
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);
