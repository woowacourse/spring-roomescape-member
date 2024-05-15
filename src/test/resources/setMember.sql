DROP TABLE reservation;
DROP TABLE user_table;

CREATE TABLE user_table
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO user_table (name, email, password, role)
VALUES ('admin', 'admin', 'admin', 'ADMIN'),
       ('name1', 'email1', 'qq1', 'USER');
