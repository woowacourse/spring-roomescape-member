SET
REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE reservation;
ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE reservation_time;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE theme;
ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE members;
ALTER TABLE members
    ALTER COLUMN id RESTART WITH 1;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00');

-- 인기 순서: 1등테마, 2등테마, 3등테마 ...
INSERT INTO theme (name, description, thumbnail)
VALUES ('1등테마', '테마 1입니다', '썸네일1'),
       ('2등테마', '테마 2입니다', '썸네일2'),
       ('3등테마', '테마 3입니다', '썸네일3'),
       ('테마4', '테마 4입니다', '썸네일4'),
       ('테마5', '테마 5입니다', '썸네일5'),
       ('테마6', '테마 6입니다', '썸네일6'),
       ('테마7', '테마 7입니다', '썸네일7'),
       ('테마8', '테마 8입니다', '썸네일8'),
       ('테마9', '테마 9입니다', '썸네일9'),
       ('테마10', '테마 10입니다', '썸네일10'),
       ('테마11', '테마 11입니다', '썸네일11');

INSERT INTO members(email, password, name, role)
VALUES ('test@email.com', 'password', '멍구', 'ADMIN');

INSERT INTO reservation (member_id, theme_id, date, time_id)
VALUES (1, 1, '2025-01-01', 1),
       (1, 1, '2025-01-01', 2),
       (1, 1, '2025-01-01', 1),
       (1, 1, '2025-01-01', 1),
       (1, 1, '2025-01-01', 1),  --테마 1 5개
       (1, 2, '2025-01-05', 1),
       (1, 2, '2025-01-05', 1),
       (1, 2, '2025-01-05', 1),
       (1, 2, '2025-01-07', 1),  -- 테마 2 4개
       (1, 3, '2025-01-07', 1);  -- 테마 3 1개
