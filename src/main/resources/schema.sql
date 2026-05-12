DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;

CREATE TABLE reservation_time (
  id BINARY(16) NOT NULL,
  start_at TIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE theme (
  id BINARY(16) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE reservation (
  id BINARY(16) NOT NULL,
  name VARCHAR(255) NOT NULL,
  date date NOT NULL,
  time_id BINARY(16) NOT NULL,
  theme_id BINARY(16) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_reservation_time_id FOREIGN KEY (time_id) REFERENCES reservation_time (id),
  CONSTRAINT fk_reservation_theme_id FOREIGN KEY (theme_id) REFERENCES theme (id),
  CONSTRAINT uq_date_time_theme UNIQUE (date, time_id, theme_id)
);
