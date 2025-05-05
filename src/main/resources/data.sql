-- 테마 추가
INSERT INTO theme (name, description, thumbnail)
VALUES ('미스터리 저택', '기묘한 사건이 벌어지는 저택을 탈출하라!', 'mystery.jpg'),
       ('사라진 시간', '시간을 거슬러 단서를 찾아라!', 'time.jpg');

-- 예약 시간 추가
INSERT INTO reservation_time (start_at)
VALUES ('14:00');

INSERT INTO users (name, email, password)
VALUES ('dompoo', 'dompoo@gmail.com', '1234'),
       ('lemon', 'lemon@gmail.com', '1234');

-- 예약 추가
-- theme_id 1에 예약 1건
INSERT INTO reservation (email, date, time_id, theme_id)
VALUES ('lemon@gmail.com', '2025-04-27', 1, 1);

-- theme_id 2에 예약 3건 (각 날짜 다르게)
INSERT INTO reservation (email, date, time_id, theme_id)
VALUES ('dompoo@gmail.com', '2025-04-24', 1, 2),
       ('dompoo@gmail.com', '2025-04-25', 1, 2),
       ('lemon@gmail.com', '2025-04-26', 1, 2);
