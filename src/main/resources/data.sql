INSERT INTO reservation_time(start_at) VALUES ('01:00');
INSERT INTO reservation_time(start_at) VALUES ('03:00');
INSERT INTO reservation_time(start_at) VALUES ('05:00');
INSERT INTO reservation_time(start_at) VALUES ('07:00');
INSERT INTO reservation_time(start_at) VALUES ('09:00');

INSERT INTO theme(name, description, thumbnail) VALUES('test1', 'desc1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test2', 'desc2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test3', 'desc3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test4', 'desc4', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member(name, email, password) VALUES ('name', 'email@email.com', 'password');
INSERT INTO member(name, role, email, password) VALUES ('어드민', 'ADMIN', 'admin@email.com', 'admin');

-- 가장 많이 예약된 테마가 3 -> 2 -> 1 -> 4 순으로 되도록 설정
-- 1번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(WEEK, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -6, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '5', '1', '1');

-- 2번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -6, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '2', '2', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '2', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -4, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '5', '2', '1');

-- 3번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '3', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '2', '3', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '3', '3', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '5', '3', '1');

-- 4번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '4', '4', '1');

