INSERT INTO theme (id, name, description, thumbnail)
VALUES (1L, '테마1', '설명1', 'https://idsncdn.iwinv.biz/news/data/20220919/p1065578397270038_168_thum.jpg');

INSERT INTO reservation_time (id, start_at)
VALUES (1L, '12:00');
INSERT INTO reservation_time (id, start_at)
VALUES (2L, '14:00');

INSERT INTO member (id, name, email, password)
VALUES (1L, '레모네', 'lemone@wooteco.com', 'lemone1234');

INSERT INTO reservation (id, reservation_date, member_id, time_id, theme_id)
VALUES (1L, '2024-05-30', 1L, 1L, 1L);
