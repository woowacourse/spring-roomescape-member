-- reservation_time
INSERT INTO reservation_time (start_at) VALUES ('10:00');

-- reservation_theme (10개)
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마1', '설명1', 'image1.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마2', '설명2', 'image2.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마3', '설명3', 'image3.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마4', '설명4', 'image4.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마5', '설명5', 'image5.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마6', '설명6', 'image6.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마7', '설명7', 'image7.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마8', '설명8', 'image8.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마9', '설명9', 'image9.jpg');
INSERT INTO reservation_theme (name, description, image_url) VALUES ('테마10', '설명10', 'image10.jpg');

-- reservation (created_at은 자동으로 오늘 날짜가 들어감)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 5);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 6);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 7);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 8);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 9);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-06', 1, 10);