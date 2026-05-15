DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS times;
DROP TABLE IF EXISTS themes;

CREATE TABLE times
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (start_at)
);

CREATE TABLE themes
(
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    name          VARCHAR(40) NOT NULL,
    thumbnail_url VARCHAR(2048),
<<<<<<< cycle2
    description   VARCHAR(400),
=======
    description   VARCHAR(40),
>>>>>>> bee9827
    PRIMARY KEY (id),
    UNIQUE (name)
);


CREATE TABLE reservations
(
<<<<<<< cycle2
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    name       VARCHAR(20) NOT NULL,
    date       DATE        NOT NULL,
    time_id    BIGINT,
    theme_id   BIGINT,
    status     VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
    deleted_at TIMESTAMP   NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES times (id),
    FOREIGN KEY (theme_id) REFERENCES themes (id),
    UNIQUE (theme_id, date, time_id, deleted_at)
=======
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    name     VARCHAR(20) NOT NULL,
    date     DATE        NOT NULL,
    time_id  BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES times (id),
    FOREIGN KEY (theme_id) REFERENCES themes (id),
    UNIQUE (theme_id, time_id, date)
>>>>>>> bee9827
);



