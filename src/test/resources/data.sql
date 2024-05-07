INSERT INTO reservation_time (start_at)
VALUES ('11:00:00'),
       ('12:00:00'),
       ('13:00:00'),
       ('14:00:00'),
       ('15:00:00')
;

INSERT INTO theme (name, description, thumbnail)
VALUES ('Theme1', 'Description for Theme1', 'thumbnail1'),
       ('Theme2', 'Description for Theme2', 'thumbnail2'),
       ('Theme3', 'Description for Theme3', 'thumbnail3'),
       ('Theme4', 'Description for Theme4', 'thumbnail4'),
       ('Theme5', 'Description for Theme5', 'thumbnail5')
;

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('User1', '2024-05-01', 1, 1),
       ('User2', '2024-05-02', 1, 1),
       ('User3', '2024-05-03', 2, 2),
       ('User4', '2024-05-04', 2, 2),
       ('User5', '2024-05-05', 3, 3)
;
