CREATE TABLE If NOT EXISTS reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reservation
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

INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (1, '김덕배', '2025-04-24', 1, 1);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (2, '김덕배2', '2025-04-25', 2, 1);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (3, '김덕배3', '2025-04-26', 3, 1);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (4, '김다배', '2025-04-27', 1, 2);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (5, '김덕배', '2025-04-28', 1, 2);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (6, '김덕배', '2025-05-01', 2, 3);
*/