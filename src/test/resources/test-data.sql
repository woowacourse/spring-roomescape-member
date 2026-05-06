INSERT INTO theme (name, description, thumbnail_url) VALUES ('워너비', '워너비 테마입니다.', 'https://example.com/wannabe.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('공포의 지하실', '지하실에서 탈출하세요.', 'https://example.com/basement.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('우주 정거장', '우주에서 살아남으세요.', 'https://example.com/space.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('탐정 사무소', '범인을 찾으세요.', 'https://example.com/detective.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('마법 학교', '마법을 배우고 탈출하세요.', 'https://example.com/magic.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('박물관이 살아있다', '밤의 박물관에서 탈출하세요.', 'https://example.com/museum.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('해적선', '보물을 찾고 탈출하세요.', 'https://example.com/pirate.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('미래 도시', '사이버펑크 세계관입니다.', 'https://example.com/cyberpunk.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('공룡 시대', '공룡들로부터 도망치세요.', 'https://example.com/dino.png');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('비밀의 정원', '아름답지만 위험한 정원입니다.', 'https://example.com/garden.png');

INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('15:00');

-- 2026-05-05에 '워너비'(ID 1) 테마 예약 (집계 대상)
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('흑곰', 1, '2026-05-05', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('카키', 1, '2026-05-05', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('피온', 1, '2026-05-05', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('워넬', 1, '2026-05-05', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('포비', 1, '2026-05-05', 5);

-- 2026-05-05에 '공포의 지하실'(ID 2) 테마 예약 (집계 대상)
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('네오', 2, '2026-05-05', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('브리', 2, '2026-05-05', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('워니', 2, '2026-05-05', 3);

-- 2026-05-06에 '워너비'(ID 1) 테마 예약 (집계 제외)
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('브라운', 1, '2026-05-06', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('제임스', 1, '2026-05-06', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('로치', 1, '2026-05-06', 5);

-- 2026-05-07에 '공포의 지하실'(ID 2) 테마 예약 (집계 제외)
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('이안', 2, '2026-05-07', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('스타크', 2, '2026-05-07', 4);
