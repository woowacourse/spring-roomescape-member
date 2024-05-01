drop table if exists theme CASCADE;
drop table if exists reservation CASCADE;
drop table if exists reservation_time CASCADE;

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

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
    theme_id BIGINT,                           -- 컬럼 수정
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

INSERT INTO theme(name, description, thumbnail) VALUES ('테바와 비밀친구', '나랑.. 비밀친구할래..?', '테바 사진 링크');
INSERT INTO theme(name, description, thumbnail) VALUES ('켈리의 댄스교실', '켈켈켈켈켈', '켈리 사진 링크');
INSERT INTO theme(name, description, thumbnail) VALUES ('우테코 탈출하기', '우테코를... 탈출..하자..!', '우테코 사진 링크');

INSERT INTO reservation_time(start_at) VALUES ('09:30');
INSERT INTO reservation_time(start_at) VALUES ('11:30');
INSERT INTO reservation_time(start_at) VALUES ('13:30');
INSERT INTO reservation_time(start_at) VALUES ('15:30');

INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('켈리', CAST(TIMESTAMPADD(DAY, -3, NOW()) AS DATE), 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('오리', CAST(TIMESTAMPADD(DAY, -4, NOW()) AS DATE), 1, 2);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('치킨보이', CAST(TIMESTAMPADD(DAY, -2, NOW()) AS DATE), 4, 1);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('치킨걸', CAST(TIMESTAMPADD(DAY, -6, NOW()) AS DATE), 3, 1);

INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('켈리', CAST(TIMESTAMPADD(DAY, 3, NOW()) AS DATE), 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('오리', CAST(TIMESTAMPADD(DAY, 4, NOW()) AS DATE), 1, 2);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('치킨보이', CAST(TIMESTAMPADD(DAY, 2, NOW()) AS DATE), 4, 1);
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('치킨걸', CAST(TIMESTAMPADD(DAY, 6, NOW()) AS DATE), 3, 1);
