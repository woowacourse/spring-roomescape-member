CREATE TABLE reservation_time (
                                  id       BIGINT       NOT NULL AUTO_INCREMENT,
                                  start_at TIME         NOT NULL,
                                  PRIMARY KEY (id),
                                  CONSTRAINT uk_reservation_time_start_at UNIQUE (start_at)
);

CREATE TABLE reservation (
                             id      BIGINT       NOT NULL AUTO_INCREMENT,
                             name    VARCHAR(255) NOT NULL,
                             date    DATE         NOT NULL,
                             time_id BIGINT,
                             PRIMARY KEY (id),
                             FOREIGN KEY (time_id) REFERENCES reservation_time (id),
                             CONSTRAINT uk_reservation_date_time UNIQUE (date, time_id)
);
