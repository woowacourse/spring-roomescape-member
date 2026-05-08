-- 실제 환경에 위치한 실 데이터
INSERT INTO reservation_time (start_at) VALUES ('10:00'); -- id 1
INSERT INTO reservation_time (start_at) VALUES ('11:00'); -- id 2
INSERT INTO reservation_time (start_at) VALUES ('12:00'); -- id 3
INSERT INTO reservation_time (start_at) VALUES ('13:00'); -- id 4

INSERT INTO theme (name, description, thumbnail_url) VALUES
('세기의 도둑', '보안을 뚫고 보석을 훔쳐라', 'https://example.com/themes/thief.jpg'),
('심해 연구소', '심해 기지를 탈출하라', 'https://example.com/themes/deepsea.jpg'),
('시간 여행자', '과거와 미래를 오가며 단서를 찾아라', 'https://example.com/themes/time.jpg'),
('유령 호텔', '폐허가 된 호텔에서 사라진 손님을 찾아라', 'https://example.com/themes/ghost-hotel.jpg'),
('비밀 실험동', '통제 구역 깊숙한 곳의 실험 기록을 회수하라', 'https://example.com/themes/lab-wing.jpg'),
('왕실 감옥', '감시를 피해 감옥의 비밀 통로를 열어라', 'https://example.com/themes/royal-prison.jpg'),
('마녀의 숲', '저주를 풀 단서를 모아 숲을 빠져나와라', 'https://example.com/themes/witch-forest.jpg'),
('우주 정거장', '고장 난 정거장을 복구하고 귀환 신호를 보내라', 'https://example.com/themes/space-station.jpg'),
('잠든 박물관', '야간 경비를 피해 전시실의 비밀을 밝혀라', 'https://example.com/themes/museum.jpg'),
('붉은 파도', '폭풍우 치는 선박 위에서 사건의 진실을 추적하라', 'https://example.com/themes/red-wave.jpg');

INSERT INTO schedule (date, time_id, theme_id) VALUES
('2026-05-05', 1, 1), -- 5월 5일 10시 세기의 도둑
('2026-05-05', 2, 1), -- 5월 5일 11시 세기의 도둑
('2026-05-05', 3, 1), -- 5월 5일 12시 세기의 도둑
('2026-05-05', 4, 1), -- 5월 5일 13시 세기의 도둑
('2026-05-06', 1, 1), -- 5월 6일 10시 세기의 도둑
('2026-05-06', 2, 1), -- 5월 6일 11시 세기의 도둑
('2026-05-06', 3, 1), -- 5월 6일 12시 세기의 도둑
('2026-05-06', 4, 1), -- 5월 6일 13시 세기의 도둑

('2026-05-05', 1, 2), -- 5월 5일 10시 심해 연구소
('2026-05-05', 2, 2), -- 5월 5일 11시 심해 연구소
('2026-05-05', 3, 2), -- 5월 5일 12시 심해 연구소
('2026-05-05', 4, 2), -- 5월 5일 13시 심해 연구소
('2026-05-06', 1, 2), -- 5월 6일 10시 심해 연구소
('2026-05-06', 2, 2), -- 5월 6일 11시 심해 연구소
('2026-05-06', 3, 2), -- 5월 6일 12시 심해 연구소
('2026-05-06', 4, 2), -- 5월 6일 13시 심해 연구소

('2026-05-05', 1, 3), -- 5월 5일 10시 시간 여행자
('2026-05-05', 2, 3), -- 5월 5일 11시 시간 여행자
('2026-05-05', 3, 3), -- 5월 5일 12시 시간 여행자
('2026-05-05', 4, 3), -- 5월 5일 13시 시간 여행자
('2026-05-06', 1, 3), -- 5월 6일 10시 시간 여행자
('2026-05-06', 2, 3), -- 5월 6일 11시 시간 여행자
('2026-05-06', 3, 3), -- 5월 6일 12시 시간 여행자
('2026-05-06', 4, 3), -- 5월 6일 13시 시간 여행자

('2026-05-05', 1, 4), -- 5월 5일 10시 유령 호텔
('2026-05-05', 2, 4), -- 5월 5일 11시 유령 호텔
('2026-05-05', 3, 4), -- 5월 5일 12시 유령 호텔
('2026-05-05', 4, 4), -- 5월 5일 13시 유령 호텔

('2026-05-05', 1, 5), -- 5월 5일 10시 비밀 실험동
('2026-05-05', 2, 5), -- 5월 5일 11시 비밀 실험동
('2026-05-05', 3, 5), -- 5월 5일 12시 비밀 실험동
('2026-05-05', 4, 5), -- 5월 5일 13시 비밀 실험동

('2026-05-05', 1, 6), -- 5월 5일 10시 왕실 감옥
('2026-05-05', 2, 6), -- 5월 5일 11시 왕실 감옥
('2026-05-05', 3, 6), -- 5월 5일 12시 왕실 감옥
('2026-05-05', 4, 6), -- 5월 5일 13시 왕실 감옥

('2026-05-05', 1, 7), -- 5월 5일 10시 마녀의 숲
('2026-05-05', 2, 7), -- 5월 5일 11시 마녀의 숲
('2026-05-05', 3, 7), -- 5월 5일 12시 마녀의 숲
('2026-05-05', 4, 7), -- 5월 5일 13시 마녀의 숲

('2026-05-05', 1, 8), -- 5월 5일 10시 우주 정거장
('2026-05-05', 2, 8), -- 5월 5일 11시 우주 정거장
('2026-05-05', 3, 8), -- 5월 5일 12시 우주 정거장
('2026-05-05', 4, 8), -- 5월 5일 13시 우주 정거장

('2026-05-05', 1, 9), -- 5월 5일 10시 잠든 박물관
('2026-05-05', 2, 9), -- 5월 5일 11시 잠든 박물관
('2026-05-05', 3, 9), -- 5월 5일 12시 잠든 박물관
('2026-05-05', 4, 9), -- 5월 5일 13시 잠든 박물관

('2026-05-05', 1, 10), -- 5월 5일 10시 붉은 파도
('2026-05-05', 2, 10), -- 5월 5일 11시 붉은 파도
('2026-05-05', 3, 10), -- 5월 5일 12시 붉은 파도
('2026-05-05', 4, 10); -- 5월 5일 13시 붉은 파도

INSERT INTO reservation (name, schedule_id)
SELECT 'kim', id FROM schedule WHERE date = '2026-05-05' AND time_id = 1 AND theme_id = 1; -- 5월 5일 10시 세기의 도둑 예약
INSERT INTO reservation (name, schedule_id)
SELECT 'kim', id FROM schedule WHERE date = '2026-05-05' AND time_id = 2 AND theme_id = 1; -- 5월 5일 11시 세기의 도둑 예약

INSERT INTO reservation (name, schedule_id)
SELECT 'lee', id FROM schedule WHERE date = '2026-05-05' AND time_id = 1 AND theme_id = 2; -- 5월 5일 10시 심해 연구소 예약
INSERT INTO reservation (name, schedule_id)
SELECT 'lee', id FROM schedule WHERE date = '2026-05-05' AND time_id = 2 AND theme_id = 2; -- 5월 5일 11시 심해 연구소 예약
INSERT INTO reservation (name, schedule_id)
SELECT 'lee', id FROM schedule WHERE date = '2026-05-05' AND time_id = 3 AND theme_id = 2; -- 5월 5일 12시 심해 연구소 예약

INSERT INTO reservation (name, schedule_id)
SELECT 'park', id FROM schedule WHERE date = '2026-05-06' AND time_id = 1 AND theme_id = 3; -- 5월 6일 10시 시간 여행자 예약
INSERT INTO reservation (name, schedule_id)
SELECT 'park', id FROM schedule WHERE date = '2026-05-06' AND time_id = 2 AND theme_id = 3; -- 5월 6일 11시 시간 여행자 예약
INSERT INTO reservation (name, schedule_id)
SELECT 'park', id FROM schedule WHERE date = '2026-05-06' AND time_id = 3 AND theme_id = 3; -- 5월 6일 12시 시간 여행자 예약
INSERT INTO reservation (name, schedule_id)
SELECT 'park', id FROM schedule WHERE date = '2026-05-06' AND time_id = 4 AND theme_id = 3; -- 5월 6일 13시 시간 여행자 예약
