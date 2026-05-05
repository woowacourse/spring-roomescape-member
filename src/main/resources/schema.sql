DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS times;
DROP TABLE IF EXISTS themes;

CREATE TABLE times
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservations
(
    id      BIGINT      NOT NULL AUTO_INCREMENT,
    name    VARCHAR(20) NOT NULL,
    date    DATE        NOT NULL,
    time_id BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES times (id),
    FOREIGN KEY (theme_id) REFERENCES themes (id)
);

CREATE TABLE themes
(
      id BIGINT NOT NULL AUTO_INCREMENT,
      name VARCHAR(40) NOT NULL,
      thumbnail_url VARCHAR(2048),
      description VARCHAR(40),
      PRIMARY KEY (id)
)


