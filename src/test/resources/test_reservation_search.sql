-- 테스트용 시간 등록
INSERT INTO reservation_time(start_at) VALUES ('01:00');

-- 테스트용 테마 등록
INSERT INTO theme(name, description, thumbnail) VALUES('test1', 'desc1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

-- 테스트용 유저 정보 등록
INSERT INTO member(name, email, password, role) VALUES ('test_member1', 'test1@email.com', 'test_password1', 'USER');

-- 지난 일주일을 기준으로 예약 생성
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');
INSERT INTO reservation(date, time_id, theme_id, member_id) VALUES (FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), '1', '1', '1');


