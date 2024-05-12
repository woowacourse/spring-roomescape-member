-- 테스트용 시간 등록
INSERT INTO reservation_time(start_at) VALUES ('01:00');
INSERT INTO reservation_time(start_at) VALUES ('03:00');
INSERT INTO reservation_time(start_at) VALUES ('05:00');
INSERT INTO reservation_time(start_at) VALUES ('07:00');
INSERT INTO reservation_time(start_at) VALUES ('09:00');

-- 테스트용 테마 등록
INSERT INTO theme(name, description, thumbnail) VALUES('test1', 'desc1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test2', 'desc2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test3', 'desc3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test4', 'desc4', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

-- 테스트용 유저 정보 등록
INSERT INTO member(name, email, password, role) VALUES ('test_member1', 'test1@email.com', 'test_password1', 'user');
INSERT INTO member(name, email, password, role) VALUES ('test_member2', 'test2@email.com', 'test_password2', 'user');
INSERT INTO member(name, email, password, role) VALUES ('test_member3', 'test3@email.com', 'test_password3', 'user');
INSERT INTO member(name, email, password, role) VALUES ('admin_member', 'admin@email.com', 'admin_password', 'admin');

-- 가장 많이 예약된 테마가 3 -> 2 -> 1 -> 4 순으로 되도록 설정
-- 1번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '5', '1', '1');

-- 2번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '2', '2', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '1', '2', '2');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '5', '2', '3');

-- 3번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '1', '3', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '2', '3', '2');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '3', '3', '3');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '5', '3', '1');

-- 4번 테마
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '4', '4', '1');


