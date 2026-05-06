INSERT INTO theme (name, thumbnail_url, description, status)
VALUES ('공포의 저택', 'https://picsum.photos/seed/horror/400/300', '어둠 속에 숨겨진 공포를 체험하세요', 'AVAILABLE');


INSERT INTO reservation_time (start_at, status)
VALUES ('10:00', 'AVAILABLE');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('user_a', '2026-04-28', 1, 1);