SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE reservation RESTART IDENTITY;
TRUNCATE TABLE reservation_time RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO reservation_time (start_at)
VALUES ('15:40'),
       ('13:40'),
       ('17:40');

INSERT INTO reservation (name, date, time_id)
VALUES ('폴라', '2023-08-05', 1),
       ('구구', '2023-06-05', 2);

