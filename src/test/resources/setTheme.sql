DROP TABLE reservation;
DROP TABLE theme;

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255),
    PRIMARY KEY (id)
);

INSERT INTO theme (name, description, thumbnail)
VALUES ('name1', 'description1', 'thumbnail1'),
       ('name2', 'description2', 'thumbnail2');
