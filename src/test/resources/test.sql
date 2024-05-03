DROP TABLE theme, reservation_time, reservation;

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(30) NOT NULL,
    date     VARCHAR(255) NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);


INSERT INTO theme (name, description, thumbnail)
VALUES ('이름1', '설명1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름2', '설명2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름3', '설명3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름4', '설명4', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름5', '설명5', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름6', '설명6', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름7', '설명7', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름8', '설명8', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름9', '설명9', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름10', '설명10', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름11', '설명11', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름12', '설명12', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('이름13', '설명13', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation_time (start_at)
VALUES ('09:00'), ('10:00'), ('11:00'), ('12:00'), ('13:00'), ('14:00'), ('15:00');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('감자', '2024-04-29', 1, 1),
       ('감자', '2024-04-28', 2, 1),
       ('감자', '2024-04-27', 3, 1),
       ('13123감자', '2024-04-29', 1, 2),
       ('감자', '2024-04-28', 1, 3),
       ('감1211자', '2024-04-29', 1, 4),
       ('312312감자', '2024-04-29', 1, 5),
       ('감자', '2024-04-29', 1, 6),
       ('감12321자', '2024-04-29', 1, 7),
       ('감자', '2024-04-29', 1, 8),
       ('111감자', '2024-04-29', 1, 9),
       ('감자', '2024-04-29', 1, 10),
       ('감자', '2024-04-29', 2, 2);
