INSERT INTO reservation_time(start_at)
VALUES ('10:00');

INSERT INTO theme(name, description, thumbnail)
VALUES ('테마1', '테마1입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-01', 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-02', 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('사용자1', '2025-05-03', 1, 1);
