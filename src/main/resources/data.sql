-- 테마
INSERT INTO theme (id, name, description, thumbnail)
VALUES (1, '공포의 저택', '무서운 이야기', 'url1');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (2, '미스터리 학교', '괴담과 미스터리', 'url2');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (3, '마법사의 방', '판타지 테마', 'url3');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (4, '우주선 탈출', 'SF 탈출', 'url4');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (5, '탐정 사무소', '추리게임', 'url5');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (6, '사라진 유물', '고대 유물 추적', 'url6');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (7, '지하 감옥', '공포감 넘치는 감옥', 'url7');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (8, '해적의 보물', '보물찾기', 'url8');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (9, '유령 열차', '소름돋는 열차 여행', 'url9');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (10, '저주받은 인형', '인형의 저주', 'url10');

-- 예약 시간
INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00');
INSERT INTO reservation_time (id, start_at)
VALUES (2, '14:00');
INSERT INTO reservation_time (id, start_at)
VALUES (3, '18:00');

-- 예약 (2025-04-22 ~ 2025-04-28 사이 날짜 기준)
-- 테마별 예약 건수를 다르게 배분
-- theme_id 1: 5건, 2: 4건, ... 10: 1건

-- theme_id = 1 (5건)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2025-04-22', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김철수', '2025-04-23', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이영희', '2025-04-24', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('박영수', '2025-04-25', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최정훈', '2025-04-26', 2, 1);

-- theme_id = 2 (4건)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김하늘', '2025-04-22', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('서지민', '2025-04-23', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('조수연', '2025-04-24', 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('장호준', '2025-04-25', 1, 2);

-- theme_id = 3 (3건)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이동현', '2025-04-22', 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('황지우', '2025-04-23', 2, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('백하윤', '2025-04-24', 3, 3);

-- theme_id = 4 (2건)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('정다희', '2025-04-25', 1, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('문경호', '2025-04-26', 2, 4);

-- theme_id = 5 (2건)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이시은', '2025-04-27', 3, 5);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('신가연', '2025-04-28', 1, 5);

-- theme_id = 6~10 (각 1건)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김서우', '2025-04-22', 1, 6);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('박민재', '2025-04-23', 2, 7);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('노은채', '2025-04-24', 3, 8);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('정하린', '2025-04-25', 1, 9);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('한지원', '2025-04-26', 2, 10);
