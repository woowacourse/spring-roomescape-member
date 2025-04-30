-- 테마 추가
INSERT INTO theme (name, description, thumbnail)
VALUES
  ('미스터리 저택', '기묘한 사건이 벌어지는 저택을 탈출하라!', 'mystery.jpg'),
  ('사라진 시간', '시간을 거슬러 단서를 찾아라!', 'time.jpg');

-- 예약 시간 추가
INSERT INTO reservation_time (start_at)
VALUES
  ('14:00');

-- 예약 추가
-- theme_id 1에 예약 1건
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES
  ('홍길동', '2025-04-27', 1, 1);

-- theme_id 2에 예약 3건 (각 날짜 다르게)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES
  ('김영희', '2025-04-24', 1, 2),
  ('박철수', '2025-04-25', 1, 2),
  ('이민정', '2025-04-26', 1, 2);
