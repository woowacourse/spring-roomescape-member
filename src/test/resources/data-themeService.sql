-- 방탈출 예약 시간 삽입
INSERT INTO reservation_time (id, start_at) VALUES (100, '10:00');


-- 테마 3종류 삽입
INSERT INTO theme (id, name, description, thumbnail) VALUES (100, '미예약', '미예약 테마입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (id, name, description, thumbnail) VALUES (101, '평범', '평범한 테마입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (id, name, description, thumbnail) VALUES (102, '추천', '추천하는 테마입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (id, name, description, thumbnail) VALUES (103, '강추', '강추하는 테마입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');


-- 방탈출 예약 삽입
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (100, '평범', '2025-05-10', 100, 101);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (101, '평범-일주일-전-예약', '2025-05-02', 100, 101);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (102, '평범-일주일-전-예약', '2025-05-01', 100, 101);

INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (103, '추천', '2025-05-10', 100, 102);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (104, '추천', '2025-05-09', 100, 102);

INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (105, '강추', '2025-05-10', 100, 103);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (106, '강추', '2025-05-09', 100, 103);
INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (107, '강추', '2025-05-08', 100, 103);
