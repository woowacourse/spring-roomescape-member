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
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

/*INSERT INTO theme (id, name, description, thumbnail) VALUES (1, '안녕, 자두야', '자두', 'https://jado.com');
INSERT INTO theme (id, name, description, thumbnail) VALUES (2, '안녕, 도기야', '도가', 'https://dogi.com');
INSERT INTO theme (id, name, description, thumbnail) VALUES (3, '안녕, 젠슨아', '젠슨', 'https://jenson.com');

INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00');
INSERT INTO reservation_time (id, start_at) VALUES (2, '11:00');
INSERT INTO reservation_time (id, start_at) VALUES (3, '12:00');
INSERT INTO reservation_time (id, start_at) VALUES (4, '13:00');

INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (1, '김덕배', '2025-05-02', 1, 1);*/