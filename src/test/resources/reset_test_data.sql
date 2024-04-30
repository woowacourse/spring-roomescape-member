DELETE FROM reservation;
ALTER TABLE reservation ALTER COLUMN id RESTART;

DELETE  FROM reservation_time;
ALTER TABLE reservation_time ALTER COLUMN id RESTART;

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('13:00');

INSERT INTO reservation (name, date, time_id)
VALUES ('브라운', '2024-05-04', 1),
       ('엘라', '2024-05-04', 2),
       ('릴리', '2023-08-05', 2);
