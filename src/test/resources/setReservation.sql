DROP TABLE reservation;
DROP TABLE theme;
DROP TABLE reservation_time;

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
    thumbnail VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('name1', 'description1', 'thumbnail1'),
       ('name2', 'description2', 'thumbnail2'),
       ('name3', 'description3', 'thumbnail3');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('first', DATEADD(DAY, 5, CURRENT_DATE), 1, 1),
       ('second', DATEADD(DAY, 6, CURRENT_DATE), 2, 2),
       ('third', DATEADD(DAY, 7, CURRENT_DATE), 2, 3),
       ('fourth', DATEADD(DAY, 8, CURRENT_DATE), 3, 1);
