DELETE FROM reservation;
DELETE FROM reservation_time;
DELETE FROM theme;

ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation_time (start_at) VALUES
('10:00:00'),
('11:00:00'),
('12:00:00'),
('13:00:00'),
('14:00:00'),
('15:00:00'),
('16:00:00'),
('17:00:00'),
('18:00:00');

INSERT INTO theme (name, description, thumbnail_url) VALUES
('우주선 탈출', '고장 난 우주선에서 제한 시간 안에 탈출하세요.', 'https://example.com/themes/space-escape.jpg'),
('좀비 아포칼립스', '봉쇄된 도시에서 생존 키트를 찾아 탈출해야 합니다.', 'https://example.com/themes/zombie-apocalypse.jpg'),
('고대 피라미드', '피라미드 깊숙한 곳의 비밀 방을 열어 보물을 찾으세요.', 'https://example.com/themes/pyramid.jpg'),
('마법학교의 비밀', '사라진 마법서를 찾아 학교의 저주를 풀어야 합니다.', 'https://example.com/themes/magic-school.jpg'),
( '해적선의 보물', '해적선 선장의 단서를 모아 숨겨진 보물창고를 여세요.', 'https://example.com/themes/pirate-treasure.jpg'),
('미스터리 연구소', '폐쇄된 연구소에서 실험 기록을 복구하고 탈출하세요.', 'https://example.com/themes/lab-mystery.jpg'),
( '시간여행자', '뒤틀린 시간 장치를 복구해 현재로 돌아오세요.', 'https://example.com/themes/time-traveler.jpg'),
( '유령의 저택', '밤이 끝나기 전 저택의 원혼을 달래는 의식을 완성하세요.', 'https://example.com/themes/haunted-mansion.jpg'),
( '사라진 화가의 작품', '실종된 화가가 남긴 암호를 풀어 진짜 작품을 찾으세요.', 'https://example.com/themes/missing-painting.jpg'),
( '심해 탐험', '산소가 떨어지기 전에 심해 기지의 전원을 복구해야 합니다.', 'https://example.com/themes/deep-sea.jpg'),
( '왕실 음모', '왕궁에서 벌어진 음모의 증거를 찾아 누명을 벗기세요.', 'https://example.com/themes/royal-conspiracy.jpg'),
( '폐병원 탈출', '버려진 병원에서 수상한 흔적을 추적해 출구를 찾으세요.', 'https://example.com/themes/abandoned-hospital.jpg'),
('한밤중의 서커스', '멈춰버린 서커스 공연의 비밀을 밝히고 무대를 탈출하세요.', 'https://example.com/themes/midnight-circus.jpg'),
('비밀 요원 작전', '이중 잠금 장치를 해제하고 기밀 문서를 회수하세요.', 'https://example.com/themes/secret-agent.jpg'),
('드래곤의 동굴', '드래곤이 잠든 사이 고대 룬을 해독해 동굴을 빠져나오세요.', 'https://example.com/themes/dragon-cave.jpg');

INSERT INTO reservation (name, reservation_date, time_id, theme_id) VALUES
-- 최근 7일 이내 20개 (기준: 2026-05-06)
('김민수', '2026-05-05', 1, 1),
('이서연', '2026-05-05', 2, 2),
('박지훈', '2026-05-05', 3, 3),
('최유진', '2026-05-05', 4, 4),
('정하늘', '2026-05-05', 5, 5),

('한지민', '2026-05-04', 1, 6),
('오세훈', '2026-05-04', 2, 7),
('윤아름', '2026-05-04', 3, 8),
('강도윤', '2026-05-04', 4, 9),
('신예린', '2026-05-04', 5, 10),

('임재현', '2026-05-03', 1, 11),
('송나연', '2026-05-03', 2, 12),
('조현우', '2026-05-03', 3, 13),
('백수진', '2026-05-03', 4, 14),
('문지호', '2026-05-03', 5, 15),

('서다은', '2026-05-02', 1, 2),
('권민재', '2026-05-01', 2, 4),
('남지수', '2026-04-30', 3, 6),
('홍예준', '2026-04-29', 4, 8),
('유다인', '2026-04-29', 5, 10),

-- 7일 이전 7개
('장태윤', '2026-04-28', 1, 3),
('노서진', '2026-04-26', 2, 5),
('류시우', '2026-04-24', 3, 7),
('배가은', '2026-04-21', 4, 9),
('안현서', '2026-04-16', 5, 11),
('구민아', '2026-04-06', 1, 13),
('차도현', '2026-03-22', 2, 15);
