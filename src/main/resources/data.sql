INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '설명1', 'https://idsncdn.iwinv.biz/news/data/20220919/p1065578397270038_168_thum.jpg');

INSERT INTO reservation_time (start_at)
VALUES ('12:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00');

INSERT INTO reservation (name, reservation_date, time_id, theme_id)
VALUES ('레모네', '2024-05-30', 1L, 1L);

INSERT INTO member (name, email, password)
VALUES ('레모네', 'lemone@wooteco.com', 'lemone1234');
