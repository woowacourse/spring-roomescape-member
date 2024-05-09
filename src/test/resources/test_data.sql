INSERT INTO reservation_time(start_at) VALUES ('01:00');

INSERT INTO theme(name, description, thumbnail) VALUES('testTheme1', 'testDesc1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('testTheme2', 'testDesc2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('testTheme3', 'testDesc3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');


-- 가장 많이 예약된 테마가 3 -> 1 -> 2 순으로 되도록 설정
-- 1번 테마
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('testName1', FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '1');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('testName2', FORMATDATETIME(TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '1');

-- 2번 테마
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('testName1', FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '2');

-- 3번 테마
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('testName1', FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '3');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('testName2', FORMATDATETIME(TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '3');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('testName3', FORMATDATETIME(TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '3');

INSERT INTO member(name, email, password) VALUES ('name', 'email@email.com', 'password');
INSERT INTO member(name, role, email, password) VALUES ('어드민', 'ADMIN', 'admin@email.com', 'admin');

