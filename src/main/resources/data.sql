INSERT INTO themes (id, name, description, thumbnail)
VALUES (1, 'Theme A', 'Desc A', 'https://picsum.photos/id/1011/200/300'),
       (2, 'Theme B', 'Desc B', 'https://picsum.photos/id/1015/200/300'),
       (3, 'Theme C', 'Desc C', 'https://picsum.photos/id/1025/200/300'),
       (4, 'Theme D', 'Desc D', 'https://picsum.photos/id/1035/200/300'),
       (5, 'Theme E', 'Desc E', 'https://picsum.photos/id/1043/200/300'),
       (6, 'Theme F', 'Desc F', 'https://picsum.photos/id/1050/200/300');

INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00:00'),
       (2, '11:00:00'),
       (3, '12:00:00'),
       (4, '13:00:00'),
       (5, '14:00:00'),
       (6, '15:00:00'),
       (7, '16:00:00'),
       (8, '17:00:00');

-- 최근 7일 윈도우 내 예약: A(4) > B(3) > C(2) > D(1)
-- Theme A: 4건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (1, 'UserA1', DATEADD('DAY', -6, CURRENT_DATE), 1, 1),
       (2, 'UserA2', DATEADD('DAY', -5, CURRENT_DATE), 1, 1),
       (3, 'UserA3', DATEADD('DAY', -4, CURRENT_DATE), 1, 1),
       (4, 'UserA4', DATEADD('DAY', -3, CURRENT_DATE), 1, 1);

-- Theme B: 3건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (5, 'UserB1', DATEADD('DAY', -6, CURRENT_DATE), 2, 2),
       (6, 'UserB2', DATEADD('DAY', -5, CURRENT_DATE), 2, 2),
       (7, 'UserB3', DATEADD('DAY', -4, CURRENT_DATE), 2, 2);

-- Theme C: 2건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (8, 'UserC1', DATEADD('DAY', -6, CURRENT_DATE), 3, 3),
       (9, 'UserC2', DATEADD('DAY', -5, CURRENT_DATE), 3, 3);

-- Theme D: 1건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (10, 'UserD1', DATEADD('DAY', -6, CURRENT_DATE), 4, 4);

-- Theme E, F: 오래된 예약 (랭킹 윈도우 밖)
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (11, 'OldUser1', DATEADD('DAY', -30, CURRENT_DATE), 5, 5),
       (12, 'OldUser2', DATEADD('DAY', -30, CURRENT_DATE), 5, 6);

ALTER TABLE themes ALTER COLUMN id RESTART WITH 20;
ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 20;
ALTER TABLE reservation ALTER COLUMN id RESTART WITH 40;