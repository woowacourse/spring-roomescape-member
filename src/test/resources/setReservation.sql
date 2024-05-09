DROP TABLE reservation;
DROP TABLE theme;
DROP TABLE reservation_time;
DROP TABLE user_table;

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

CREATE TABLE user_table
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    date VARCHAR(255) NOT NULL,
    member_id BIGINT NOT NULL,
    time_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES user_table (id)
);

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('name1', 'description1', 'thumbnail1'),
       ('name2', 'description2', 'thumbnail2'),
       ('name3', 'description3', 'thumbnail3');

INSERT INTO user_table (name, email, password, role)
VALUES ('admin', 'admin', 'admin', 'ADMIN'),
       ('name1', 'email1', 'qq1', 'USER');

INSERT INTO reservation (date, member_id, time_id, theme_id)
VALUES (DATEADD(DAY, 5, CURRENT_DATE), 1, 1, 1),
       (DATEADD(DAY, 6, CURRENT_DATE), 1, 2, 2),
       (DATEADD(DAY, 7, CURRENT_DATE), 2, 2, 3),
       (DATEADD(DAY, 8, CURRENT_DATE), 2, 3, 1);
