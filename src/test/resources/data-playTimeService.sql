-- 방탈출 예약 시간 삽입
INSERT INTO reservation_time (id, start_at) VALUES (100, '10:00');
INSERT INTO reservation_time (id, start_at) VALUES (101, '12:00');


-- 테마 3종류 삽입
INSERT INTO theme (id, name, description, thumbnail) VALUES (100, '평범', '평범한 테마입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');


-- 방탈출 예약 삽입
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (100, '평범', '2025-05-10', 100, 100);
