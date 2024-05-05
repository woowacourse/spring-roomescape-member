INSERT INTO reservation_time(start_at) VALUES ('01:00');

INSERT INTO theme(name, description, thumbnail) VALUES('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('브라운', FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), '1', '1');
