-- 테이블이 이미 존재할 경우를 대비해 삭제
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;
DROP TABLE IF EXISTS member;

-- 멤버 테이블
CREATE TABLE member (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id)
);

-- 테마 테이블
CREATE TABLE theme (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- 예약 시간 테이블
CREATE TABLE reservation_time (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- 예약 테이블 (member_id 외래키 추가, name 제거)
CREATE TABLE reservation (
    id         BIGINT NOT NULL AUTO_INCREMENT,
    date       DATE   NOT NULL,
    member_id  BIGINT NOT NULL,
    time_id    BIGINT NOT NULL,
    theme_id   BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (time_id) REFERENCES reservation_time(id),
    FOREIGN KEY (theme_id) REFERENCES theme(id)
);
