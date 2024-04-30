INSERT INTO reservation_time (start_at)
VALUES ('10:15'),
       ('11:20'),
       ('12:25');

INSERT INTO theme (name, description, thumbnail)
VALUES ('spring', 'Escape from spring cold', 'Spring thumb'),
       ('summer', 'Escape from hottest weather', 'Summer thumb');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('al', '2025-01-20', 1, 1),
       ('be', '2025-02-19', 2, 2);
