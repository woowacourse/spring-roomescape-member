drop table if exists reservation CASCADE;
drop table if exists reservation_time CASCADE;

CREATE TABLE reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT,                           -- 컬럼 수정
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id) -- 외래키 추가
);

INSERT INTO reservation_time(start_at) VALUES ('10:10');
INSERT INTO reservation_time(start_at) VALUES ('11:30');

INSERT INTO reservation(name, date, time_id) VALUES ('켈리', '2024-07-03', 1);
INSERT INTO reservation(name, date, time_id) VALUES ('오리', '2024-07-03', 1);
