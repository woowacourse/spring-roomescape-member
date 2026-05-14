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
('우주선 탈출',        '고장 난 우주선에서 제한 시간 안에 탈출하세요.',             'https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=600'),
('좀비 아포칼립스',    '봉쇄된 도시에서 생존 키트를 찾아 탈출해야 합니다.',         'https://images.unsplash.com/photo-1509248961158-e54f6934749c?w=600'),
('고대 피라미드',      '피라미드 깊숙한 곳의 비밀 방을 열어 보물을 찾으세요.',       'https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?w=600'),
('마법학교의 비밀',    '사라진 마법서를 찾아 학교의 저주를 풀어야 합니다.',           'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=600'),
('해적선의 보물',      '해적선 선장의 단서를 모아 숨겨진 보물창고를 여세요.',         'https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=600'),
('미스터리 연구소',    '폐쇄된 연구소에서 실험 기록을 복구하고 탈출하세요.',          'https://images.unsplash.com/photo-1532187863486-abf9dbad1b69?w=600'),
('시간여행자',         '뒤틀린 시간 장치를 복구해 현재로 돌아오세요.',               'https://images.unsplash.com/photo-1501139083538-0139583c060f?w=600'),
('유령의 저택',        '밤이 끝나기 전 저택의 원혼을 달래는 의식을 완성하세요.',      'https://images.unsplash.com/photo-1509557965875-b88c97052f0e?w=600'),
('사라진 화가의 작품', '실종된 화가가 남긴 암호를 풀어 진짜 작품을 찾으세요.',       'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?w=600'),
('심해 탐험',          '산소가 떨어지기 전에 심해 기지의 전원을 복구해야 합니다.',    'https://images.unsplash.com/photo-1518020382113-a7e8fc38eac9?w=600'),
('왕실 음모',          '왕궁에서 벌어진 음모의 증거를 찾아 누명을 벗기세요.',         'https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=600'),
('폐병원 탈출',        '버려진 병원에서 수상한 흔적을 추적해 출구를 찾으세요.',       'https://images.unsplash.com/photo-1516574187841-cb9cc2ca948b?w=600'),
('한밤중의 서커스',    '멈춰버린 서커스 공연의 비밀을 밝히고 무대를 탈출하세요.',     'https://images.unsplash.com/photo-1576872381149-7847515ce5d8?w=600'),
('비밀 요원 작전',     '이중 잠금 장치를 해제하고 기밀 문서를 회수하세요.',           'https://images.unsplash.com/photo-1453873531674-2151bcd01707?w=600'),
('드래곤의 동굴',      '드래곤이 잠든 사이 고대 룬을 해독해 동굴을 빠져나오세요.',    'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=600');


INSERT INTO reservation (name, reservation_date, time_id, theme_id, status) VALUES

-- 오늘
('진리로', '2026-05-14', 1, 1,  'RESERVED'),
('진리로', '2026-05-14', 7, 2,  'RESERVED'),
('진리로', '2026-05-14', 8, 3,  'RESERVED'),
('진리로', '2026-05-14', 9, 4,  'RESERVED'),



-- 최근 7일 이내 (인기 테마 집계 포함) ─ 모두 COMPLETED
('김민수', '2026-05-13', 1, 1,  'COMPLETED'),
('정하늘', '2026-05-13', 5, 5,  'COMPLETED'),
('이서연', '2026-05-12', 2, 1,  'COMPLETED'),
('최유진', '2026-05-12', 4, 4,  'COMPLETED'),
('정하늘', '2026-05-12', 5, 5,  'COMPLETED'),
('이서연', '2026-05-11', 3, 1,  'COMPLETED'),
('최유진', '2026-05-10', 4, 4,  'CANCELED'),
('김민수', '2026-05-09', 1, 1,  'COMPLETED'),
('이서연', '2026-05-08', 2, 2,  'COMPLETED'),
('박지훈', '2026-05-07', 3, 3,  'COMPLETED'),

-- 7일 이전 (과거) ─ COMPLETED, 일부 CANCELED
('김민수', '2026-05-05', 1, 1,  'COMPLETED'),
('이서연', '2026-05-05', 2, 2,  'COMPLETED'),
('박지훈', '2026-05-05', 3, 3,  'COMPLETED'),
('최유진', '2026-05-05', 4, 4,  'COMPLETED'),
('정하늘', '2026-05-05', 5, 5,  'COMPLETED'),

('한지민', '2026-05-04', 1, 6,  'COMPLETED'),
('오세훈', '2026-05-04', 2, 7,  'CANCELED'),
('윤아름', '2026-05-04', 3, 8,  'COMPLETED'),
('강도윤', '2026-05-04', 4, 9,  'COMPLETED'),
('신예린', '2026-05-04', 5, 10, 'CANCELED'),

('임재현', '2026-05-03', 1, 11, 'COMPLETED'),
('송나연', '2026-05-03', 2, 12, 'COMPLETED'),
('조현우', '2026-05-03', 3, 13, 'COMPLETED'),
('백수진', '2026-05-03', 4, 14, 'CANCELED'),
('문지호', '2026-05-03', 5, 15, 'COMPLETED'),

('서다은', '2026-05-02', 1, 2,  'COMPLETED'),
('권민재', '2026-05-01', 2, 4,  'COMPLETED'),
('남지수', '2026-04-30', 3, 6,  'COMPLETED'),
('홍예준', '2026-04-29', 4, 8,  'COMPLETED'),
('유다인', '2026-04-29', 5, 10, 'CANCELED'),

('장태윤', '2026-04-28', 1, 3,  'COMPLETED'),
('노서진', '2026-04-26', 2, 5,  'COMPLETED'),
('류시우', '2026-04-24', 3, 7,  'COMPLETED'),
('이서연', '2026-04-21', 4, 9,  'COMPLETED'),
('안현서', '2026-04-16', 5, 11, 'COMPLETED'),
('구민아', '2026-04-06', 1, 13, 'COMPLETED'),
('차도현', '2026-03-22', 2, 15, 'COMPLETED'),

-- 미래 예약 (RESERVED) ─ 예약 페이지·Admin 수정·취소 테스트용
('김민수', '2026-05-20', 1, 1,  'RESERVED'),
('이서연', '2026-05-20', 3, 2,  'RESERVED'),
('박지훈', '2026-05-21', 2, 3,  'RESERVED'),
('최유진', '2026-05-21', 4, 5,  'RESERVED'),
('정하늘', '2026-05-22', 5, 4,  'RESERVED'),
('한지민', '2026-05-23', 6, 6,  'RESERVED'),
('오세훈', '2026-05-24', 7, 7,  'RESERVED'),
('김민수', '2026-06-13', 1, 1,  'RESERVED'),
('정하늘', '2026-06-13', 5, 5,  'RESERVED'),
('이서연', '2026-06-12', 2, 1,  'RESERVED'),
('최유진', '2026-06-12', 4, 4,  'RESERVED'),
('정하늘', '2026-06-12', 5, 5,  'RESERVED'),
('이서연', '2026-06-11', 3, 1,  'RESERVED'),
('최유진', '2026-06-10', 4, 4,  'RESERVED'),
('김민수', '2026-06-09', 1, 1,  'CANCELED'),
('이서연', '2026-06-08', 2, 2,  'RESERVED'),
('박지훈', '2026-06-07', 3, 3,  'RESERVED'),
('정하늘', '2026-06-13', 5, 5,  'RESERVED'),
('이서연', '2026-06-12', 2, 1,  'RESERVED'),
('최유진', '2026-06-12', 4, 4,  'RESERVED'),
('정하늘', '2026-06-12', 5, 5,  'RESERVED'),
('이서연', '2026-06-11', 3, 1,  'RESERVED'),
('최유진', '2026-06-10', 4, 4,  'RESERVED'),
('김민수', '2026-06-09', 1, 1,  'RESERVED'),
('이서연', '2026-06-08', 2, 2,  'RESERVED'),
('박지훈', '2026-06-07', 3, 3,  'RESERVED')