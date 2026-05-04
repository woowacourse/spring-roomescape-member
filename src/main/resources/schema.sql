CREATE TABLE IF NOT EXISTS reservation_time (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
    );

MERGE INTO reservation_time(start_at) KEY (start_at) VALUES
    ('10:00'), ('10:10'), ('10:20'), ('10:30'), ('10:40'), ('10:50'),
    ('11:00'), ('11:10'), ('11:20'), ('11:30'), ('11:40'), ('11:50'),
    ('12:00'), ('12:10'), ('12:20'), ('12:30'), ('12:40'), ('12:50'),
    ('13:00'), ('13:10'), ('13:20'), ('13:30'), ('13:40'), ('13:50'),
    ('14:00'), ('14:10'), ('14:20'), ('14:30'), ('14:40'), ('14:50'),
    ('15:00'), ('15:10'), ('15:20'), ('15:30'), ('15:40'), ('15:50'),
    ('16:00'), ('16:10'), ('16:20'), ('16:30'), ('16:40'), ('16:50'),
    ('17:00'), ('17:10'), ('17:20'), ('17:30'), ('17:40'), ('17:50'),
    ('18:00'), ('18:10'), ('18:20'), ('18:30'), ('18:40'), ('18:50'),
    ('19:00'), ('19:10'), ('19:20'), ('19:30'), ('19:40'), ('19:50'),
    ('20:00'), ('20:10'), ('20:20'), ('20:30');

CREATE TABLE IF NOT EXISTS reservation (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    date    VARCHAR(255) NOT NULL,
    time_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id)
);
