DELETE
FROM reservation_time;

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('13:00'),
       ('16:00');
