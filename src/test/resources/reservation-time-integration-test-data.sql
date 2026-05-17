DELETE FROM reservation;
DELETE FROM reservation_time;
DELETE FROM theme;

ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation_time (start_at) VALUES ('12:00'), ('14:30');
INSERT INTO theme (name, description, image_url)
VALUES ('테마1', '설명1', 'https://example.com/img.jpg');

-- time_id=1은 내일 예약 있음 (오늘 이후이므로 삭제 불가)
-- time_id=2는 예약 없음 (삭제 가능)
INSERT INTO reservation (name, res_date, time_id, theme_id)
VALUES ('코로구', DATEADD('DAY', 1, CURRENT_DATE), 1, 1);
