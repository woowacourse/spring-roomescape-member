DELETE
FROM reservation;

DELETE
FROM reservation_time;

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('13:00'),
       ('16:00');

DELETE
FROM theme;

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO theme (name, description, thumbnail_url, runtime)
VALUES ('테마1', '테마1 설명', 'https://example.com/themes/theme-1.png', 60),
       ('테마2', '테마2 설명', 'https://example.com/themes/theme-2.png', 60),
       ('테마3', '테마3 설명', 'https://example.com/themes/theme-3.png', 60),
       ('테마4', '테마4 설명', 'https://example.com/themes/theme-4.png', 60),
       ('테마5', '테마5 설명', 'https://example.com/themes/theme-5.png', 60),
       ('테마6', '테마6 설명', 'https://example.com/themes/theme-6.png', 60),
       ('테마7', '테마7 설명', 'https://example.com/themes/theme-7.png', 60),
       ('테마8', '테마8 설명', 'https://example.com/themes/theme-8.png', 60),
       ('테마9', '테마9 설명', 'https://example.com/themes/theme-9.png', 60),
       ('테마10', '테마10 설명', 'https://example.com/themes/theme-10.png', 60),
       ('테마11', '테마11 설명', 'https://example.com/themes/theme-11.png', 60),
       ('테마12', '테마12 설명', 'https://example.com/themes/theme-12.png', 60),
       ('테마13', '테마13 설명', 'https://example.com/themes/theme-13.png', 60),
       ('테마14', '테마14 설명', 'https://example.com/themes/theme-14.png', 60),
       ('테마15', '테마15 설명', 'https://example.com/themes/theme-15.png', 60);

ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES
    -- 테마1: 1개
    ('예약자1', '2026-05-01', 1, 1),

    -- 테마2: 2개
    ('예약자2', '2026-05-01', 1, 2),
    ('예약자3', '2026-05-01', 2, 2),

    -- 테마3: 3개
    ('예약자4', '2026-05-01', 1, 3),
    ('예약자5', '2026-05-01', 2, 3),
    ('예약자6', '2026-05-01', 3, 3),

    -- 테마4: 4개
    ('예약자7', '2026-05-01', 1, 4),
    ('예약자8', '2026-05-01', 2, 4),
    ('예약자9', '2026-05-01', 3, 4),
    ('예약자10', '2026-05-02', 1, 4),

    -- 테마5: 5개
    ('예약자11', '2026-05-01', 1, 5),
    ('예약자12', '2026-05-01', 2, 5),
    ('예약자13', '2026-05-01', 3, 5),
    ('예약자14', '2026-05-02', 1, 5),
    ('예약자15', '2026-05-02', 2, 5);
