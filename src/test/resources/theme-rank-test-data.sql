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

INSERT INTO member
VALUES (1, 'naknak');

INSERT INTO member_credential
VALUES (1, 1, 'naknak@example.com', 'nak123');

INSERT INTO reservation (id, member_id, date, reservation_time_id, theme_id)
VALUES (1, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 5),
       (2, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 5),
       (3, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 5),
       (4, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 5),
       (5, 1, CURRENT_DATE - INTERVAL '1' DAY, 5, 5),
       (6, 1, CURRENT_DATE - INTERVAL '1' DAY, 6, 5),
       (7, 1, CURRENT_DATE - INTERVAL '1' DAY, 7, 5),
       (8, 1, CURRENT_DATE - INTERVAL '1' DAY, 8, 5),
       (9, 1, CURRENT_DATE - INTERVAL '1' DAY, 9, 5),
       (10, 1, CURRENT_DATE - INTERVAL '1' DAY, 10, 5),
       (11, 1, CURRENT_DATE - INTERVAL '1' DAY, 11, 5),

       (12, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 2),
       (13, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 2),
       (14, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 2),
       (15, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 2),
       (16, 1, CURRENT_DATE - INTERVAL '1' DAY, 5, 2),
       (17, 1, CURRENT_DATE - INTERVAL '1' DAY, 6, 2),
       (18, 1, CURRENT_DATE - INTERVAL '1' DAY, 7, 2),
       (19, 1, CURRENT_DATE - INTERVAL '1' DAY, 8, 2),
       (20, 1, CURRENT_DATE - INTERVAL '1' DAY, 9, 2),
       (21, 1, CURRENT_DATE - INTERVAL '1' DAY, 10, 2),

       (22, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 3),
       (23, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 3),
       (24, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 3),
       (25, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 3),
       (26, 1, CURRENT_DATE - INTERVAL '1' DAY, 5, 3),
       (27, 1, CURRENT_DATE - INTERVAL '1' DAY, 6, 3),
       (28, 1, CURRENT_DATE - INTERVAL '1' DAY, 7, 3),
       (29, 1, CURRENT_DATE - INTERVAL '1' DAY, 8, 3),
       (30, 1, CURRENT_DATE - INTERVAL '1' DAY, 9, 3),

       (31, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 7),
       (32, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 7),
       (33, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 7),
       (34, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 7),
       (35, 1, CURRENT_DATE - INTERVAL '1' DAY, 5, 7),
       (36, 1, CURRENT_DATE - INTERVAL '1' DAY, 6, 7),
       (37, 1, CURRENT_DATE - INTERVAL '1' DAY, 7, 7),
       (38, 1, CURRENT_DATE - INTERVAL '1' DAY, 8, 7),

       (39, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 10),
       (40, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 10),
       (41, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 10),
       (42, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 10),
       (43, 1, CURRENT_DATE - INTERVAL '1' DAY, 5, 10),
       (44, 1, CURRENT_DATE - INTERVAL '1' DAY, 6, 10),
       (45, 1, CURRENT_DATE - INTERVAL '1' DAY, 7, 10),

       (46, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 1),
       (47, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 1),
       (48, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 1),
       (49, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 1),
       (50, 1, CURRENT_DATE - INTERVAL '1' DAY, 5, 1),
       (51, 1, CURRENT_DATE - INTERVAL '1' DAY, 6, 1),

       (52, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 4),
       (53, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 4),
       (54, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 4),
       (55, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 4),
       (56, 1, CURRENT_DATE - INTERVAL '1' DAY, 5, 4),

       (57, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 6),
       (58, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 6),
       (59, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 6),
       (60, 1, CURRENT_DATE - INTERVAL '1' DAY, 4, 6),

       (61, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 8),
       (62, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 8),
       (63, 1, CURRENT_DATE - INTERVAL '1' DAY, 3, 8),

       (64, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 9),
       (65, 1, CURRENT_DATE - INTERVAL '1' DAY, 2, 9),

       (66, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 11),
       (67, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 12),
       (68, 1, CURRENT_DATE - INTERVAL '1' DAY, 1, 13);
