CREATE TABLE reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT,
    theme_id BIGINT,                                        -- 컬럼 추가
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)            -- 외래키 추가
);

-- insert into reservation_time (start_at) values ('12:00'); --1
-- insert into reservation_time (start_at) values ('13:00'); --2
-- insert into reservation_time (start_at) values ('14:00'); --3
--
-- insert into theme (name, description, thumbnail) values ('테마1','설명1','https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
-- insert into theme (name, description, thumbnail) values ('테마2','설명2','https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
-- insert into theme (name, description, thumbnail) values ('테마3','설명3','https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
--
-- insert into reservation ( name, date, time_id, theme_id) values ('벡터','2025-04-30',1,1);
-- insert into reservation ( name, date, time_id, theme_id) values ('벡터','2025-04-29',1,1);
-- insert into reservation ( name, date, time_id, theme_id) values ('벡터','2025-04-28',1,1);
--
-- insert into reservation ( name, date, time_id, theme_id) values ('띠용','2025-04-30',2,2);
-- insert into reservation ( name, date, time_id, theme_id) values ('띠용','2025-04-29',2,2);
-- insert into reservation ( name, date, time_id, theme_id) values ('띠용','2025-04-22',2,2);
-- insert into reservation ( name, date, time_id, theme_id) values ('띠용','2025-04-21',2,2);
--
-- insert into reservation ( name, date, time_id, theme_id) values ('띠터','2025-04-30',3,3);
