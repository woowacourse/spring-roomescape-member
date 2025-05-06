INSERT INTO reservation_time(start_at)
VALUES ('10:00');
INSERT INTO reservation_time(start_at)
VALUES ('11:00');
INSERT INTO reservation_time(start_at)
VALUES ('12:00');

INSERT INTO theme(name, description, thumbnail)
VALUES ('테마1', '테마1입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마2', '테마2입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마3', '테마3입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마4', '테마4입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

-- 테마1은 0501 ~ 0503 동안 3번 예약됨
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-01', 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-02', 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-03', 1, 1);

-- 테마2는 0501 ~ 0503 동안 2번 예약됨
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-01', 2, 2);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-02', 2, 2);

-- 테마3은 0501 ~ 0503 동안 1번 예약됨
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-01', 3, 3);

-- 테마3은 0401 ~ 0404 동안 4번 예약됨
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-04-01', 1, 4);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-04-02', 1, 4);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-04-03', 1, 4);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-04-04', 1, 4);
