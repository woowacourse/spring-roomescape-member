-- 예약 시간
INSERT INTO reservation_time (start_at, end_at) VALUES ('10:00:00', '11:30:00');
INSERT INTO reservation_time (start_at, end_at) VALUES ('12:00:00', '13:30:00');
INSERT INTO reservation_time (start_at, end_at) VALUES ('14:00:00', '15:30:00');
INSERT INTO reservation_time (start_at, end_at) VALUES ('16:00:00', '17:30:00');
INSERT INTO reservation_time (start_at, end_at) VALUES ('18:00:00', '19:30:00');
INSERT INTO reservation_time (start_at, end_at) VALUES ('20:00:00', '21:30:00');

-- 테마
INSERT INTO theme (name, description, thumbnail_url) VALUES (
    '망각의 정신병원',
    '오래된 폐정신병원에 갇혔다. 깜빡이는 형광등, 복도 끝에서 들려오는 발소리... 여기서 탈출하려면 이 병원의 어두운 비밀을 파헤쳐야 한다.',
    '/images/theme_asylum.png'
);
INSERT INTO theme (name, description, thumbnail_url) VALUES (
    '스테이션 제로',
    '심우주 정거장에서 혼자 잠에서 깨어났다. 승무원들은 어디로 갔을까? 산소가 고갈되기 전에 통신을 복구하고 탈출 포드를 가동해야 한다.',
    '/images/theme_space.png'
);
INSERT INTO theme (name, description, thumbnail_url) VALUES (
    '파라오의 금고',
    '고대 이집트의 비밀 지하 묘실에 발을 들였다. 함정과 암호로 가득한 파라오의 저주를 풀지 못하면 이 돌무덤이 영원한 안식처가 된다.',
    '/images/theme_pharaoh.png'
);
INSERT INTO theme (name, description, thumbnail_url) VALUES (
    '크림슨 레터',
    '1940년대 뒷골목의 사설탐정 사무소. 테이블 위의 붉은 봉투에는 연쇄 살인마의 단서가 담겨 있다. 다음 피해자가 되기 전에 사건을 해결하라.',
    '/images/theme_detective.png'
);

-- 샘플 예약 (오늘 날짜 기준 미래 날짜를 고정값으로 넣기 어려우므로 과거 기준 참고용)
-- 실제 운영 시 날짜를 조정하거나 삭제하세요.
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', DATEADD('DAY', 1, CURRENT_DATE), 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김철수', DATEADD('DAY', 1, CURRENT_DATE), 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이영희', DATEADD('DAY', 2, CURRENT_DATE), 2, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박민준', DATEADD('DAY', 3, CURRENT_DATE), 5, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('최지우', DATEADD('DAY', 1, CURRENT_DATE), 4, 1);
