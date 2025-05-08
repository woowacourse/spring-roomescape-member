INSERT INTO reservation_time(id, start_at)
VALUES (1, '10:00'),
       (2, '11:00'),
       (3, '12:00');

INSERT INTO theme(id, name, description, thumbnail)
VALUES (1, '테마1', '설명1', 'thumbnail1.jpg'),
       (2, '테마2', '설명2', 'thumbnail2.jpg'),
       (3, '테마3', '설명3', 'thumbnail3.jpg');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('예약1', '2025-04-25', 1, 1),
       ('예약2', '2025-04-25', 1, 2);

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 4;

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 4;

ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 3;
