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

-- 2026-05-01 ~ 2026-05-06 사이 예약
-- Theme A: 6건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (1, 'User1', '2026-05-01', 1, 1),
       (2, 'User2', '2026-05-02', 2, 1),
       (3, 'User3', '2026-05-03', 3, 1),
       (4, 'User4', '2026-05-04', 4, 1),
       (5, 'User5', '2026-05-05', 1, 1),
       (6, 'User6', '2026-05-06', 5, 1);

-- Theme B: 4건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (7, 'User7', '2026-05-01', 2, 2),
       (8, 'User8', '2026-05-02', 3, 2),
       (9, 'User9', '2026-05-06', 4, 2),
       (10, 'User10', '2026-05-03', 5, 2);

-- Theme C: 2건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (11, 'User11', '2026-05-06', 1, 3),
       (12, 'User12', '2026-05-02', 2, 3);

-- Theme D: 1건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (13, 'User13', '2026-05-05', 3, 4);

-- Theme E: 3건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (14, 'User14', '2026-05-02', 6, 5),
       (15, 'User15', '2026-05-04', 7, 5),
       (16, 'User16', '2026-05-06', 8, 5);

-- Theme F: 2건
INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (17, 'User17', '2026-05-03', 6, 6),
       (18, 'User18', '2026-05-05', 7, 6);

ALTER TABLE themes
    ALTER COLUMN id RESTART WITH 20;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 20;
ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 40;
