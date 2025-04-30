-- 테이블이 이미 존재할 경우를 대비해 삭제
DROP TABLE IF EXISTS theme;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS reservation;

-- 테마 테이블
CREATE TABLE theme (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- 예약 시간 테이블
CREATE TABLE reservation_time (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    start_at  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- 예약 테이블
CREATE TABLE reservation (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    date    VARCHAR(255) NOT NULL,
    time_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time(id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);
