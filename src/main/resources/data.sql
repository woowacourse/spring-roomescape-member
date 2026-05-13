INSERT IGNORE INTO `user` (id, name, role) VALUES (1, '루크', 'ADMIN');
INSERT IGNORE INTO `user` (id, name, role) VALUES (2, '소낙눈', 'USER');
INSERT IGNORE INTO `user` (id, name, role) VALUES (3, '포비', 'USER');

INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (1, '공포', '등골이 오싹한 공포 테마', 'https://i.pinimg.com/736x/b3/4a/d4/b34ad4fd5bcbced41d7f340c539cd4d7.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (2, '로맨스', '두근두근 로맨스 테마', 'https://i.pinimg.com/1200x/c2/a3/00/c2a30020e8c1a25f7032d7d360886de7.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (3, '감성', '눈물 쏙 빼는 감성 테마', 'https://i.pinimg.com/736x/af/d0/9c/afd09c1c478137db8da6c29d610cc693.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (4, '스릴러', '심장이 쫄깃한 스릴러 테마', 'https://i.pinimg.com/736x/be/c9/d6/bec9d6c70028d43f8a4c00330c51c516.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (5, '스토리', '탄탄한 세계관의 스토리 테마', 'https://i.pinimg.com/1200x/78/85/b7/7885b7216437601598a21bca10751bec.jpg', '02:00:00');

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1, 1, FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd 12:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (2, 1, FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd 13:00:00'), FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd 15:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (3, 1, FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd 16:00:00'), FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd 18:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (4, 1, FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd 19:00:00'), FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd 21:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (5, 2, FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd 12:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (6, 2, FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd 13:00:00'), FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd 15:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (7, 2, FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd 16:00:00'), FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd 18:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (8, 3, FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd 12:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (9, 3, FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd 13:00:00'), FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd 15:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (10, 4, FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd 16:00:00'), FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd 18:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (11, 5, FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd 12:00:00'));

INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (1, 1, 1);
INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (2, 2, 2);
INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (3, 3, 3);
INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (4, 1, 4);

INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (5, 2, 5);
INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (6, 3, 6);
INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (7, 1, 7);

INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (8, 2, 8);
INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (9, 3, 9);

INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (10, 1, 10);

INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (11, 2, 11);

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (12, 1, '2026-05-20 10:00:00', '2026-05-20 12:00:00');

INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (100, '테스트테마', '테스트용', 'test.jpg', '01:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1000, 100, '2026-06-01 10:00:00', '2026-06-01 11:00:00');

INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (101, '예약테마', '예약용', 'reserved.jpg', '01:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1001, 101, '2026-06-02 10:00:00', '2026-06-02 11:00:00');
INSERT IGNORE INTO `user` (id, name, role) VALUES (101, '테스트유저', 'USER');
INSERT IGNORE INTO reservation (id, user_id, schedule_id) VALUES (10000, 101, 1001);

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (13, 1, '2026-06-25 10:00:00', '2026-06-25 12:00:00');

-- ==========================================
-- 🗓️ 프론트엔드 달력 테스트용 스케줄 (2026년 5월~6월)
-- 테마: 1번(공포), 시간: 13:00 ~ 15:00
-- ==========================================
-- 5월
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20501, 1, '2026-05-01 13:00:00', '2026-05-01 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20502, 1, '2026-05-02 13:00:00', '2026-05-02 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20503, 1, '2026-05-03 13:00:00', '2026-05-03 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20504, 1, '2026-05-04 13:00:00', '2026-05-04 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20505, 1, '2026-05-05 13:00:00', '2026-05-05 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20506, 1, '2026-05-06 13:00:00', '2026-05-06 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20507, 1, '2026-05-07 13:00:00', '2026-05-07 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20508, 1, '2026-05-08 13:00:00', '2026-05-08 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20509, 1, '2026-05-09 13:00:00', '2026-05-09 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20510, 1, '2026-05-10 13:00:00', '2026-05-10 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20511, 1, '2026-05-11 13:00:00', '2026-05-11 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20512, 1, '2026-05-12 13:00:00', '2026-05-12 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20513, 1, '2026-05-13 13:00:00', '2026-05-13 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20514, 1, '2026-05-14 13:00:00', '2026-05-14 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20515, 1, '2026-05-15 13:00:00', '2026-05-15 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20516, 1, '2026-05-16 13:00:00', '2026-05-16 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20517, 1, '2026-05-17 13:00:00', '2026-05-17 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20518, 1, '2026-05-18 13:00:00', '2026-05-18 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20519, 1, '2026-05-19 13:00:00', '2026-05-19 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20520, 1, '2026-05-20 13:00:00', '2026-05-20 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20521, 1, '2026-05-21 13:00:00', '2026-05-21 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20522, 1, '2026-05-22 13:00:00', '2026-05-22 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20523, 1, '2026-05-23 13:00:00', '2026-05-23 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20524, 1, '2026-05-24 13:00:00', '2026-05-24 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20525, 1, '2026-05-25 13:00:00', '2026-05-25 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20526, 1, '2026-05-26 13:00:00', '2026-05-26 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20527, 1, '2026-05-27 13:00:00', '2026-05-27 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20528, 1, '2026-05-28 13:00:00', '2026-05-28 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20529, 1, '2026-05-29 13:00:00', '2026-05-29 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20530, 1, '2026-05-30 13:00:00', '2026-05-30 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20531, 1, '2026-05-31 13:00:00', '2026-05-31 15:00:00');

-- 6월
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20601, 1, '2026-06-01 13:00:00', '2026-06-01 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20602, 1, '2026-06-02 13:00:00', '2026-06-02 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20603, 1, '2026-06-03 13:00:00', '2026-06-03 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20604, 1, '2026-06-04 13:00:00', '2026-06-04 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20605, 1, '2026-06-05 13:00:00', '2026-06-05 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20606, 1, '2026-06-06 13:00:00', '2026-06-06 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20607, 1, '2026-06-07 13:00:00', '2026-06-07 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20608, 1, '2026-06-08 13:00:00', '2026-06-08 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20609, 1, '2026-06-09 13:00:00', '2026-06-09 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20610, 1, '2026-06-10 13:00:00', '2026-06-10 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20611, 1, '2026-06-11 13:00:00', '2026-06-11 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20612, 1, '2026-06-12 13:00:00', '2026-06-12 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20613, 1, '2026-06-13 13:00:00', '2026-06-13 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20614, 1, '2026-06-14 13:00:00', '2026-06-14 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20615, 1, '2026-06-15 13:00:00', '2026-06-15 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20616, 1, '2026-06-16 13:00:00', '2026-06-16 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20617, 1, '2026-06-17 13:00:00', '2026-06-17 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20618, 1, '2026-06-18 13:00:00', '2026-06-18 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20619, 1, '2026-06-19 13:00:00', '2026-06-19 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20620, 1, '2026-06-20 13:00:00', '2026-06-20 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20621, 1, '2026-06-21 13:00:00', '2026-06-21 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20622, 1, '2026-06-22 13:00:00', '2026-06-22 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20623, 1, '2026-06-23 13:00:00', '2026-06-23 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20624, 1, '2026-06-24 13:00:00', '2026-06-24 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20625, 1, '2026-06-25 13:00:00', '2026-06-25 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20626, 1, '2026-06-26 13:00:00', '2026-06-26 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20627, 1, '2026-06-27 13:00:00', '2026-06-27 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20628, 1, '2026-06-28 13:00:00', '2026-06-28 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20629, 1, '2026-06-29 13:00:00', '2026-06-29 15:00:00');
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (20630, 1, '2026-06-30 13:00:00', '2026-06-30 15:00:00');