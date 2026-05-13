CREATE TABLE theme(
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    thumbnail_image_url VARCHAR(255) NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    duration_time VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
);

CREATE TABLE reservation_time (
                                  id       BIGINT       NOT NULL AUTO_INCREMENT,
                                  start_at VARCHAR(255) NOT NULL,
                                  PRIMARY KEY (id)
);
CREATE TABLE reservation (
                             id      BIGINT       NOT NULL AUTO_INCREMENT,
                             name    VARCHAR(255) NOT NULL,
                             date    VARCHAR(255) NOT NULL,
                             time_id BIGINT,
                             theme_id BIGINT,
                             status  VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
                             deleted_at TIMESTAMP,
                             PRIMARY KEY (id),
                             FOREIGN KEY (time_id) REFERENCES reservation_time (id),
                             FOREIGN KEY (theme_id) REFERENCES theme (id)
);

CREATE UNIQUE INDEX unique_active_reservation
ON reservation (date, time_id, theme_id, deleted_at);
