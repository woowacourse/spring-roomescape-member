INSERT IGNORE INTO `user` (id, name, role) VALUES (1, '루크', 'ADMIN');
INSERT IGNORE INTO `user` (id, name, role) VALUES (2, '소낙눈', 'USER');
INSERT IGNORE INTO `user` (id, name, role) VALUES (3, '포비', 'USER');

INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (1, '공포', '등골이 오싹한 공포 테마', 'https://i.pinimg.com/736x/b3/4a/d4/b34ad4fd5bcbced41d7f340c539cd4d7.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (2, '로맨스', '두근두근 로맨스 테마', 'https://i.pinimg.com/1200x/c2/a3/00/c2a30020e8c1a25f7032d7d360886de7.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (3, '감성', '눈물 쏙 빼는 감성 테마', 'https://i.pinimg.com/736x/af/d0/9c/afd09c1c478137db8da6c29d610cc693.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (4, '스릴러', '심장이 쫄깃한 스릴러 테마', 'https://i.pinimg.com/736x/be/c9/d6/bec9d6c70028d43f8a4c00330c51c516.jpg', '02:00:00');
INSERT IGNORE INTO theme (id, name, description, image_url, required_time) VALUES (5, '스토리', '탄탄한 세계관의 스토리 테마', 'https://i.pinimg.com/1200x/78/85/b7/7885b7216437601598a21bca10751bec.jpg', '02:00:00');

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1001, 1, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 12:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1002, 1, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 13:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 15:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1003, 1, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 16:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 18:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1004, 2, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 12:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1005, 2, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 14:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 16:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1006, 2, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 18:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 20:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1007, 3, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 11:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 13:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1008, 3, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 15:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 17:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1009, 3, FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 19:00:00'), FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-dd 21:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1010, 4, FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 12:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1011, 4, FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 13:00:00'), FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 15:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1012, 4, FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 16:00:00'), FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 18:00:00'));

INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1013, 5, FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 10:00:00'), FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 12:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1014, 5, FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 14:00:00'), FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 16:00:00'));
INSERT IGNORE INTO schedule (id, theme_id, start_at, end_at) VALUES (1015, 5, FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 18:00:00'), FORMATDATETIME(DATEADD(DAY, 1, CURRENT_DATE), 'yyyy-MM-dd 20:00:00'));

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
