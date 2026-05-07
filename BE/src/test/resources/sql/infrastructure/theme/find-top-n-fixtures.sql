INSERT INTO reservation_time (start_at) VALUES ('10:00:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00:00');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('인기 테마', '설명1', 'thumb1');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('덜 인기 테마', '설명2', 'thumb2');
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('예약자1', '2026-05-01', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('예약자2', '2026-05-02', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('예약자3', '2026-05-03', 3, 2);
