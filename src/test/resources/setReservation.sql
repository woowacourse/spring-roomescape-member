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
VALUES ('first', '2024-05-09', 1, 1),
       ('second', '2024-05-10', 2, 2),
       ('third', '2024-05-11', 2, 3),
       ('fourth', '2024-05-12', 3, 1);
