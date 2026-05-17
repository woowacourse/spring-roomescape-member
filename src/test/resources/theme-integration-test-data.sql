DELETE FROM reservation;
DELETE FROM reservation_time;
DELETE FROM theme;

ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO theme (name, description, image_url)
VALUES ('테마1', '설명1', 'https://example.com/img.jpg'),
       ('테마2', '설명2', 'https://example.com/img.jpg'),
       ('테마3', '설명3', 'https://example.com/img.jpg');

-- theme_id=1: 미래 예약 있음 (삭제 불가)
-- theme_id=2: 예약 없음 (삭제 가능)
-- theme_id=3: 과거 주간 예약 있음 (주간 인기 테마 집계 대상)
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('코로구', DATEADD('DAY', 1, CURRENT_DATE), 1, 1),
       ('브라운', DATEADD('DAY', -1, CURRENT_DATE), 1, 3),
       ('잠봉', DATEADD('DAY', -2, CURRENT_DATE), 1, 3);
