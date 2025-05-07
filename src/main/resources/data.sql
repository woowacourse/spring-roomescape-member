-- 테마 추가
INSERT INTO theme (id, name, description, thumbnail)
VALUES ('1', '미스터리 저택', '기묘한 사건이 벌어지는 저택을 탈출하라!', 'mystery.jpg'),
       ('2', '사라진 시간', '시간을 거슬러 단서를 찾아라!', 'time.jpg');

-- 예약 시간 추가
INSERT INTO reservation_time (id, start_at)
VALUES ('3', '14:00');

INSERT INTO users (id, role, name, email, password)
VALUES ('4', 'USER', 'dompoo', 'dompoo@gmail.com', '1234'),
       ('5', 'USER', 'lemon', 'lemon@gmail.com', '1234'),
       ('6', 'ADMIN', 'admin', 'admin@gmail.com', '1234');

-- 예약 추가
-- theme_id 1에 예약 1건
INSERT INTO reservation (id, date, time_id, theme_id, user_id)
VALUES ('7', '2025-04-27', '3', '2', '4');

-- theme_id 2에 예약 3건 (각 날짜 다르게)
INSERT INTO reservation (id, date, time_id, theme_id, user_id)
VALUES ('8', '2025-04-24', '3', '1', '4'),
       ('9', '2025-04-25', '3', '2', '5'),
       ('10', '2025-04-26', '3', '2', '5');
