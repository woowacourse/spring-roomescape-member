-- reservation_time
INSERT INTO reservation_time (start_at) VALUES ('10:00');

-- theme (10개)
INSERT INTO theme (name, description, image_url) VALUES ('테마1', '설명1', 'https://picsum.photos/id/135/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마2', '설명2', 'https://picsum.photos/id/136/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마3', '설명3', 'https://picsum.photos/id/137/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마4', '설명4', 'https://picsum.photos/id/145/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마5', '설명5', 'https://picsum.photos/id/139/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마6', '설명6', 'https://picsum.photos/id/140/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마7', '설명7', 'https://picsum.photos/id/141/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마8', '설명8', 'https://picsum.photos/id/142/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마9', '설명9', 'https://picsum.photos/id/143/200');
INSERT INTO theme (name, description, image_url) VALUES ('테마10', '설명10', 'https://picsum.photos/id/144/200');

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