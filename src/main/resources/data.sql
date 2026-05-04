INSERT INTO reservation_time (start_at)
VALUES ('10:00');

INSERT INTO reservation_time (start_at)
VALUES ('11:00');

INSERT INTO theme (name, description, image_url)
VALUES ('테마 이름', '테마 설명', 'https://roomescape.com/images/themes/ring-banner.png');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('피온', '2026-05-04', 1, 1);