ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation_time (start_at)
VALUES ('01:00'),
       ('02:00'),
       ('03:00'),
       ('04:00'),
       ('05:00'),
       ('06:00'),
       ('07:00'),
       ('08:00'),
       ('09:00'),
       ('10:00');

INSERT INTO theme (name, description, image_url)
VALUES ('테마0', '테마0', 'https://example.com/img.jpg'),
       ('테마1', '테마1', 'https://example.com/img.jpg'),
       ('테마2', '테마2', 'https://example.com/img.jpg'),
       ('테마3', '테마3', 'https://example.com/img.jpg'),
       ('테마4', '테마4', 'https://example.com/img.jpg'),
       ('테마5', '테마5', 'https://example.com/img.jpg'),
       ('테마6', '테마6', 'https://example.com/img.jpg'),
       ('테마7', '테마7', 'https://example.com/img.jpg'),
       ('테마8', '테마8', 'https://example.com/img.jpg'),
       ('테마9', '테마9', 'https://example.com/img.jpg');

-- 테마i에 i+1개씩 예약 (테마9가 가장 많음)
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 1);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 2),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 2);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 3),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 3),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 3);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 4),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 4),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 4),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 4, 4);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 5),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 5),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 5),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 4, 5),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 5, 5);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 6),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 6),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 6),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 4, 6),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 5, 6),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 6, 6);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 7),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 7),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 7),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 4, 7),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 5, 7),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 6, 7),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 7, 7);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 8),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 8),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 8),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 4, 8),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 5, 8),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 6, 8),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 7, 8),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 8, 8);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 4, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 5, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 6, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 7, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 8, 9),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 9, 9);
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('name', DATEADD('DAY', -1, CURRENT_DATE), 1, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 2, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 3, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 4, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 5, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 6, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 7, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 8, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 9, 10),
       ('name', DATEADD('DAY', -1, CURRENT_DATE), 10, 10);
