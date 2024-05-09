INSERT INTO theme
VALUES (1, 'theme1', 'description1', 'thumbnail1'),
       (2, 'theme2', 'description2', 'thumbnail2'),
       (3, 'theme3', 'description3', 'thumbnail3'),
       (4, 'theme4', 'description4', 'thumbnail4'),
       (5, 'theme5', 'description5', 'thumbnail5'),
       (6, 'theme6', 'description6', 'thumbnail6'),
       (7, 'theme7', 'description7', 'thumbnail7'),
       (8, 'theme8', 'description8', 'thumbnail8'),
       (9, 'theme9', 'description9', 'thumbnail9'),
       (10, 'theme10', 'description10', 'thumbnail10'),
       (11, 'theme11', 'description11', 'thumbnail11'),
       (12, 'theme12', 'description12', 'thumbnail12'),
       (13, 'theme13', 'description13', 'thumbnail13');


INSERT INTO reservation_time
VALUES (1, '10:00'),
       (2, '11:00'),
       (3, '12:00'),
       (4, '13:00'),
       (5, '14:00'),
       (6, '15:00'),
       (7, '16:00'),
       (8, '17:00'),
       (9, '18:00'),
       (10, '19:00'),
       (11, '20:00');

INSERT INTO reservation (id, name, date, reservation_time_id, theme_id)
VALUES (1, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 1, 5),
       (2, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 2, 5),
       (3, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 3, 5),
       (4, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 4, 5),
       (5, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 5, 5),
       (6, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 6, 5),
       (7, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 7, 5),
       (8, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 8, 5),
       (9, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 9, 5),
       (10, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 10, 5),
       (11, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 11, 5),

       (12, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 1, 2),
       (13, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 2, 2),
       (14, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 3, 2),
       (15, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 4, 2),
       (16, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 5, 2),
       (17, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 6, 2),
       (18, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 7, 2),
       (19, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 8, 2),
       (20, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 9, 2),
       (21, 'resevation', CURRENT_DATE - INTERVAL '1' DAY, 10, 2),

       (22, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 3),
       (23, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 3),
       (24, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 3, 3),
       (25, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 4, 3),
       (26, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 5, 3),
       (27, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 6, 3),
       (28, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 7, 3),
       (29, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 8, 3),
       (30, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 9, 3),

       (31, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 7),
       (32, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 7),
       (33, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 3, 7),
       (34, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 4, 7),
       (35, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 5, 7),
       (36, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 6, 7),
       (37, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 7, 7),
       (38, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 8, 7),

       (39, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 10),
       (40, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 10),
       (41, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 3, 10),
       (42, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 4, 10),
       (43, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 5, 10),
       (44, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 6, 10),
       (45, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 7, 10),

       (46, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 1),
       (47, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 1),
       (48, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 3, 1),
       (49, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 4, 1),
       (50, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 5, 1),
       (51, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 6, 1),

       (52, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 4),
       (53, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 4),
       (54, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 3, 4),
       (55, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 4, 4),
       (56, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 5, 4),

       (57, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 6),
       (58, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 6),
       (59, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 3, 6),
       (60, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 4, 6),

       (61, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 8),
       (62, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 8),
       (63, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 3, 8),

       (64, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 9),
       (65, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 2, 9),

       (66, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 11),
       (67, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 12),
       (68, 'reservation', CURRENT_DATE - INTERVAL '1' DAY, 1, 13);
