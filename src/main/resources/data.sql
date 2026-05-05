-- RESERVATION_TIME: 10:00 ~ 20:00 (1시간 단위, 11개)
INSERT INTO RESERVATION_TIME (start_at) VALUES ('10:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('11:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('12:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('13:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('14:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('15:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('16:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('17:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('18:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('19:00');
INSERT INTO RESERVATION_TIME (start_at) VALUES ('20:00');

-- THEME: 5개 (인기도 차별화)
INSERT INTO THEME (name, description, thumbnail_url) VALUES ('공포의 저택', '으스스한 저택에서 탈출하세요', 'https://example.com/horror.jpg');
INSERT INTO THEME (name, description, thumbnail_url) VALUES ('우주 탐험', '광활한 우주의 비밀을 풀어보세요', 'https://example.com/space.jpg');
INSERT INTO THEME (name, description, thumbnail_url) VALUES ('마법 학교', '마법 학교의 숨겨진 비밀을 찾아라', 'https://example.com/magic.jpg');
INSERT INTO THEME (name, description, thumbnail_url) VALUES ('고대 유적', '고대 문명의 유적을 탐험하세요', 'https://example.com/ancient.jpg');
INSERT INTO THEME (name, description, thumbnail_url) VALUES ('탐정 사무소', '미스터리 사건을 해결하세요', 'https://example.com/detective.jpg');

-- RESERVATION: 30개 (2026-04-28 ~ 2026-05-04)
-- Theme 1 (공포의 저택): 10건 → 1위
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('김철수', '2026-04-28', 1, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('이영희', '2026-04-28', 2, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('박민수', '2026-04-29', 3, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('최지원', '2026-04-29', 4, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('정수진', '2026-04-30', 5, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('한동훈', '2026-04-30', 6, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('임채원', '2026-05-01', 7, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('오세훈', '2026-05-02', 8, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('신지아', '2026-05-03', 9, 1);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('유민호', '2026-05-04', 10, 1);

-- Theme 2 (우주 탐험): 8건 → 2위
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('강민준', '2026-04-28', 3, 2);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('조현아', '2026-04-29', 4, 2);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('윤지호', '2026-04-30', 5, 2);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('장서연', '2026-05-01', 6, 2);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('황준혁', '2026-05-01', 7, 2);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('송미래', '2026-05-02', 8, 2);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('안태양', '2026-05-03', 9, 2);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('배소희', '2026-05-04', 10, 2);

-- Theme 3 (마법 학교): 6건 → 3위
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('권지훈', '2026-04-29', 1, 3);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('나예린', '2026-04-30', 2, 3);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('도현승', '2026-05-01', 3, 3);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('류지아', '2026-05-02', 4, 3);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('마하은', '2026-05-03', 5, 3);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('서태양', '2026-05-04', 6, 3);

-- Theme 4 (고대 유적): 4건 → 4위
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('엄지원', '2026-04-30', 7, 4);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('전현무', '2026-05-01', 8, 4);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('천서진', '2026-05-02', 9, 4);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('표민혁', '2026-05-03', 10, 4);

-- Theme 5 (탐정 사무소): 2건 → 5위
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('허가은', '2026-05-01', 11, 5);
INSERT INTO RESERVATION (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-04', 1, 5);
