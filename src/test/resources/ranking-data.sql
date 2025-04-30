SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE reservation;
ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE reservation_time;
ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE theme;
ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO reservation_time (id, start_at)
VALUES
    (1, '10:00'),
    (2, '11:00'),
    (3, '12:00');

INSERT INTO theme (id, name, description, thumbnail)
VALUES
    (1, '테마1', '테마 1입니다', '썸네일1'),
    (2, '테마2', '테마 2입니다', '썸네일2'),
    (3, '테마3', '테마 3입니다', '썸네일3');

INSERT INTO reservation (name, theme_id, date, time_id)
VALUES
    ('브라운', 1, '2024-04-01', 1),
    ('솔라',   2, '2024-04-02', 2),
    ('브리',   2, '2024-04-03', 1),
    ('브리',   3, '2024-04-04', 1),
    ('브리',   3, '2024-04-05', 1),
    ('브리',   3, '2024-04-06', 1);
