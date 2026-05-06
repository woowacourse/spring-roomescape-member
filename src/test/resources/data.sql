INSERT INTO theme (id, name, description, thumbnail_image_url)
VALUES (1, '우테코 방탈출', '우테코 시그니처 테마입니다.', 'https://example.com/thumbnails/theme1.png'),
       (2, '저주받은 인형', '공포 장르 테마입니다.', 'https://example.com/thumbnails/theme2.png'),
       (3, '미스터리 살인사건', '추리 장르 테마입니다.', 'https://example.com/thumbnails/theme3.png');

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 10;

INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00:00'),
       (2, '11:00:00'),
       (3, '12:00:00'),
       (4, '13:00:00');

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 10;

INSERT INTO reservation (id, name, theme_id, date, time_id)
VALUES (1, '김철수', 1, '2026-05-06', 1),
       (2, '이영희', 1, '2026-05-06', 2),
       (3, '박민수', 2, '2026-05-06', 1),
       (4, '최수진', 1, '2026-05-07', 1);

ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 100;
