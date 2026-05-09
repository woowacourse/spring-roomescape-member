INSERT INTO users (id, name, email)
VALUES (1, '예약자', 'user@test.com');

ALTER TABLE users
    ALTER COLUMN id RESTART WITH 10;

INSERT INTO theme (id, name, description, thumbnail_image_url)
VALUES (1, '우테코 방탈출', '우테코 시그니처 테마입니다.', 'https://example.com/thumbnails/theme1.png'),
       (2, '저주받은 인형', '공포 장르 테마입니다.', 'https://example.com/thumbnails/theme2.png'),
       (3, '미스터리 살인사건', '추리 장르 테마입니다.', 'https://example.com/thumbnails/theme3.png'),
       (4, '우주 탐험', '우주 배경 SF 테마입니다.', 'https://example.com/thumbnails/theme4.png'),
       (5, '중세 성의 비밀', '중세 판타지 테마입니다.', 'https://example.com/thumbnails/theme5.png'),
       (6, '마법사의 탑', '마법 세계 테마입니다.', 'https://example.com/thumbnails/theme6.png'),
       (7, '좀비 아포칼립스', '좀비 서바이벌 테마입니다.', 'https://example.com/thumbnails/theme7.png'),
       (8, '탐정 사무소', '노아르 추리 테마입니다.', 'https://example.com/thumbnails/theme8.png'),
       (9, '해저 탐험', '심해 탐험 테마입니다.', 'https://example.com/thumbnails/theme9.png'),
       (10, '시간 여행', '타임루프 테마입니다.', 'https://example.com/thumbnails/theme10.png'),
       (11, '지하 던전', '다크 판타지 테마입니다.', 'https://example.com/thumbnails/theme11.png'),
       (12, '마피아 하우스', '사회적 추리 테마입니다.', 'https://example.com/thumbnails/theme12.png'),
       (13, '빙하 기지', '빙하 SF 테마입니다.', 'https://example.com/thumbnails/theme13.png'),
       (14, '화산 탈출', '재난 탈출 테마입니다.', 'https://example.com/thumbnails/theme14.png'),
       (15, '사막의 신전', '고대 문명 테마입니다.', 'https://example.com/thumbnails/theme15.png');

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 20;

INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00:00'),
       (2, '11:00:00'),
       (3, '12:00:00'),
       (4, '13:00:00');

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 10;

-- 집계 기간: CURRENT_DATE-7(from) ~ CURRENT_DATE-1(to)
-- 기간 내 데이터: CURRENT_DATE-2 ~ CURRENT_DATE-6 (안전하게 중간 범위 사용)
-- 기간 외 데이터: CURRENT_DATE-15 이상 (from보다 훨씬 이전)
INSERT INTO reservation (id, user_id, theme_id, date, time_id)
VALUES
    -- 테마 1: 10건 (기간 내)
    (1, 1, 1, DATEADD('DAY', -2, CURRENT_DATE), 1),
    (2, 1, 1, DATEADD('DAY', -2, CURRENT_DATE), 2),
    (3, 1, 1, DATEADD('DAY', -2, CURRENT_DATE), 3),
    (4, 1, 1, DATEADD('DAY', -2, CURRENT_DATE), 4),
    (5, 1, 1, DATEADD('DAY', -3, CURRENT_DATE), 1),
    (6, 1, 1, DATEADD('DAY', -3, CURRENT_DATE), 2),
    (7, 1, 1, DATEADD('DAY', -3, CURRENT_DATE), 3),
    (8, 1, 1, DATEADD('DAY', -3, CURRENT_DATE), 4),
    (9, 1, 1, DATEADD('DAY', -4, CURRENT_DATE), 1),
    (10, 1, 1, DATEADD('DAY', -4, CURRENT_DATE), 2),
    -- 테마 2: 9건 (기간 내)
    (11, 1, 2, DATEADD('DAY', -2, CURRENT_DATE), 1),
    (12, 1, 2, DATEADD('DAY', -2, CURRENT_DATE), 2),
    (13, 1, 2, DATEADD('DAY', -2, CURRENT_DATE), 3),
    (14, 1, 2, DATEADD('DAY', -2, CURRENT_DATE), 4),
    (15, 1, 2, DATEADD('DAY', -3, CURRENT_DATE), 1),
    (16, 1, 2, DATEADD('DAY', -3, CURRENT_DATE), 2),
    (17, 1, 2, DATEADD('DAY', -3, CURRENT_DATE), 3),
    (18, 1, 2, DATEADD('DAY', -3, CURRENT_DATE), 4),
    (19, 1, 2, DATEADD('DAY', -4, CURRENT_DATE), 1),
    -- 테마 3: 8건 (기간 내)
    (20, 1, 3, DATEADD('DAY', -2, CURRENT_DATE), 1),
    (21, 1, 3, DATEADD('DAY', -2, CURRENT_DATE), 2),
    (22, 1, 3, DATEADD('DAY', -2, CURRENT_DATE), 3),
    (23, 1, 3, DATEADD('DAY', -2, CURRENT_DATE), 4),
    (24, 1, 3, DATEADD('DAY', -3, CURRENT_DATE), 1),
    (25, 1, 3, DATEADD('DAY', -3, CURRENT_DATE), 2),
    (26, 1, 3, DATEADD('DAY', -3, CURRENT_DATE), 3),
    (27, 1, 3, DATEADD('DAY', -3, CURRENT_DATE), 4),
    -- 테마 4: 7건 (기간 내)
    (28, 1, 4, DATEADD('DAY', -2, CURRENT_DATE), 1),
    (29, 1, 4, DATEADD('DAY', -2, CURRENT_DATE), 2),
    (30, 1, 4, DATEADD('DAY', -2, CURRENT_DATE), 3),
    (31, 1, 4, DATEADD('DAY', -2, CURRENT_DATE), 4),
    (32, 1, 4, DATEADD('DAY', -3, CURRENT_DATE), 1),
    (33, 1, 4, DATEADD('DAY', -3, CURRENT_DATE), 2),
    (34, 1, 4, DATEADD('DAY', -3, CURRENT_DATE), 3),
    -- 테마 5: 6건 (기간 내)
    (35, 1, 5, DATEADD('DAY', -2, CURRENT_DATE), 1),
    (36, 1, 5, DATEADD('DAY', -2, CURRENT_DATE), 2),
    (37, 1, 5, DATEADD('DAY', -2, CURRENT_DATE), 3),
    (38, 1, 5, DATEADD('DAY', -2, CURRENT_DATE), 4),
    (39, 1, 5, DATEADD('DAY', -3, CURRENT_DATE), 1),
    (40, 1, 5, DATEADD('DAY', -3, CURRENT_DATE), 2),
    -- 테마 6: 5건 (기간 내)
    (41, 1, 6, DATEADD('DAY', -2, CURRENT_DATE), 1),
    (42, 1, 6, DATEADD('DAY', -2, CURRENT_DATE), 2),
    (43, 1, 6, DATEADD('DAY', -2, CURRENT_DATE), 3),
    (44, 1, 6, DATEADD('DAY', -2, CURRENT_DATE), 4),
    (45, 1, 6, DATEADD('DAY', -3, CURRENT_DATE), 1),
    -- 테마 7: 4건 (기간 내)
    (46, 1, 7, DATEADD('DAY', -4, CURRENT_DATE), 1),
    (47, 1, 7, DATEADD('DAY', -4, CURRENT_DATE), 2),
    (48, 1, 7, DATEADD('DAY', -4, CURRENT_DATE), 3),
    (49, 1, 7, DATEADD('DAY', -4, CURRENT_DATE), 4),
    -- 테마 8: 4건 (기간 내)
    (50, 1, 8, DATEADD('DAY', -5, CURRENT_DATE), 1),
    (51, 1, 8, DATEADD('DAY', -5, CURRENT_DATE), 2),
    (52, 1, 8, DATEADD('DAY', -5, CURRENT_DATE), 3),
    (53, 1, 8, DATEADD('DAY', -5, CURRENT_DATE), 4),
    -- 테마 9: 3건 (기간 내)
    (54, 1, 9, DATEADD('DAY', -6, CURRENT_DATE), 1),
    (55, 1, 9, DATEADD('DAY', -6, CURRENT_DATE), 2),
    (56, 1, 9, DATEADD('DAY', -6, CURRENT_DATE), 3),
    -- 테마 10: 3건 (기간 내)
    (57, 1, 10, DATEADD('DAY', -6, CURRENT_DATE), 2),
    (58, 1, 10, DATEADD('DAY', -6, CURRENT_DATE), 3),
    (59, 1, 10, DATEADD('DAY', -6, CURRENT_DATE), 4),
    -- 테마 11: 2건 (기간 내)
    (60, 1, 11, DATEADD('DAY', -5, CURRENT_DATE), 1),
    (61, 1, 11, DATEADD('DAY', -5, CURRENT_DATE), 2),
    -- 테마 12: 2건 (기간 내)
    (62, 1, 12, DATEADD('DAY', -5, CURRENT_DATE), 3),
    (63, 1, 12, DATEADD('DAY', -5, CURRENT_DATE), 4),
    -- 테마 13: 2건 (기간 내)
    (64, 1, 13, DATEADD('DAY', -5, CURRENT_DATE), 1),
    (65, 1, 13, DATEADD('DAY', -5, CURRENT_DATE), 2),
    -- 테마 14: 1건 (기간 내)
    (66, 1, 14, DATEADD('DAY', -4, CURRENT_DATE), 3),
    -- 테마 15: 1건 (기간 내)
    (67, 1, 15, DATEADD('DAY', -4, CURRENT_DATE), 4),
    -- 기간 외 예약 (from=CURRENT_DATE-7 보다 이전) - 인기 테마 집계에서 제외되어야 함
    -- 테마 13~15에 기간 외 예약을 많이 추가해 기간 필터 동작 검증
    (68, 1, 13, DATEADD('DAY', -15, CURRENT_DATE), 1),
    (69, 1, 13, DATEADD('DAY', -15, CURRENT_DATE), 2),
    (70, 1, 13, DATEADD('DAY', -15, CURRENT_DATE), 3),
    (71, 1, 13, DATEADD('DAY', -15, CURRENT_DATE), 4),
    (72, 1, 13, DATEADD('DAY', -16, CURRENT_DATE), 1),
    (73, 1, 13, DATEADD('DAY', -16, CURRENT_DATE), 2),
    (74, 1, 14, DATEADD('DAY', -15, CURRENT_DATE), 1),
    (75, 1, 14, DATEADD('DAY', -15, CURRENT_DATE), 2),
    (76, 1, 14, DATEADD('DAY', -15, CURRENT_DATE), 3),
    (77, 1, 14, DATEADD('DAY', -15, CURRENT_DATE), 4),
    (78, 1, 15, DATEADD('DAY', -16, CURRENT_DATE), 1),
    (79, 1, 15, DATEADD('DAY', -16, CURRENT_DATE), 2),
    (80, 1, 15, DATEADD('DAY', -16, CURRENT_DATE), 3);

ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 100;
