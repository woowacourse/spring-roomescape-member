SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE reservation RESTART IDENTITY;
TRUNCATE TABLE reservation_time RESTART IDENTITY;
TRUNCATE TABLE reservation_date RESTART IDENTITY;
TRUNCATE TABLE theme RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;
    
-- 예약 시간 등록
INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');

-- 예약 날짜 등록 (오늘 기준 최근 7일 이내)
INSERT INTO reservation_date (date) VALUES (DATEADD('DAY', -1, CURRENT_DATE));
INSERT INTO reservation_date (date) VALUES (DATEADD('DAY', -2, CURRENT_DATE));
INSERT INTO reservation_date (date) VALUES (DATEADD('DAY', -3, CURRENT_DATE));
INSERT INTO reservation_date (date) VALUES (DATEADD('DAY', -4, CURRENT_DATE));
INSERT INTO reservation_date (date) VALUES (DATEADD('DAY', -5, CURRENT_DATE));

-- 테마 등록
INSERT INTO theme (name, description, thumbnail_url, is_active) VALUES ('공포', '공포 테마 설명', 'https://horror.jpg', true);
INSERT INTO theme (name, description, thumbnail_url, is_active) VALUES ('판타지', '판타지 테마 설명', 'https://fantasy.jpg', true);
INSERT INTO theme (name, description, thumbnail_url, is_active) VALUES ('미스터리', '미스터리 테마 설명', 'https://mystery.jpg', true);

-- 공포 테마 예약 5개 (1위)
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('한다', DATEADD('DAY', -1, CURRENT_DATE), '10:00', 1, 'RESERVED');
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('판다', DATEADD('DAY', -2, CURRENT_DATE), '10:00', 1, 'RESERVED');
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('브라운', DATEADD('DAY', -3, CURRENT_DATE), '10:00', 1, 'RESERVED');
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('송송', DATEADD('DAY', -4, CURRENT_DATE), '10:00', 1, 'RESERVED');
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('루루', DATEADD('DAY', -5, CURRENT_DATE), '10:00', 1, 'RESERVED');

-- 판타지 테마 예약 3개 (2위)
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('한다', DATEADD('DAY', -1, CURRENT_DATE), '12:00', 2, 'RESERVED');
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('판다', DATEADD('DAY', -2, CURRENT_DATE), '12:00', 2, 'RESERVED');
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('브라운', DATEADD('DAY', -3, CURRENT_DATE), '12:00', 2, 'RESERVED');

-- 미스터리 테마 예약 1개 (3위)
INSERT INTO reservation (name, date, start_at, theme_id, status) VALUES ('한다', DATEADD('DAY', -1, CURRENT_DATE), '14:00', 3, 'RESERVED');
