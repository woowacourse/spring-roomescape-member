INSERT INTO reservation_time(start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00'),
       ('14:00'),
       ('15:00'),
       ('16:00'),
       ('17:00'),
       ('18:00'),
       ('19:00'),
       ('20:00'),
       ('21:00'),
       ('22:00'),
       ('23:00'),
       ('23:06');

INSERT INTO theme(name, description, thumbnail)
VALUES ('Theme 1', '테마1 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 2', '테마2 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 3', '테마3 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 4', '테마4 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 5', '테마5 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 6', '테마6 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 7', '테마7 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 8', '테마8 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 9', '테마9 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 10', '테마10 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 11', '테마11 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 12', '테마12 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU'),
       ('Theme 13', '테마13 설명',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQqlkX2ISwyii-yHkQmp-Ad0hsfekERx2RNEa_RFNrr25BDWEAxHRgghcPid7ckxbLngE&usqp=CAU');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES
-- theme_id 1에 1개 예약
('예약1', '2025-04-25', 1, 1),

-- theme_id 2에 2개 예약
('예약2', '2025-04-25', 1, 2),
('예약3', '2025-04-25', 2, 2),

-- theme_id 3에 3개 예약
('예약4', '2025-04-25', 1, 3),
('예약5', '2025-04-25', 2, 3),
('예약6', '2025-04-25', 3, 3),

-- theme_id 4에 4개 예약
('예약7', '2025-04-25', 1, 4),
('예약8', '2025-04-25', 2, 4),
('예약9', '2025-04-25', 3, 4),
('예약10', '2025-04-25', 4, 4),

-- theme_id 5에 5개 예약
('예약11', '2025-04-25', 1, 5),
('예약12', '2025-04-25', 2, 5),
('예약13', '2025-04-25', 3, 5),
('예약14', '2025-04-25', 4, 5),
('예약15', '2025-04-25', 5, 5),

-- theme_id 6에 6개 예약
('예약16', '2025-04-25', 1, 6),
('예약17', '2025-04-25', 2, 6),
('예약18', '2025-04-25', 3, 6),
('예약19', '2025-04-25', 4, 6),
('예약20', '2025-04-25', 5, 6),
('예약21', '2025-04-25', 6, 6),

-- theme_id 7에 7개 예약
('예약22', '2025-04-25', 1, 7),
('예약23', '2025-04-25', 2, 7),
('예약24', '2025-04-25', 3, 7),
('예약25', '2025-04-25', 4, 7),
('예약26', '2025-04-25', 5, 7),
('예약27', '2025-04-25', 6, 7),
('예약28', '2025-04-25', 7, 7),

-- theme_id 8에 8개 예약
('예약29', '2025-04-25', 1, 8),
('예약30', '2025-04-25', 2, 8),
('예약31', '2025-04-25', 3, 8),
('예약32', '2025-04-25', 4, 8),
('예약33', '2025-04-25', 5, 8),
('예약34', '2025-04-25', 6, 8),
('예약35', '2025-04-25', 7, 8),
('예약36', '2025-04-25', 8, 8),

-- theme_id 9에 9개 예약
('예약37', '2025-04-25', 1, 9),
('예약38', '2025-04-25', 2, 9),
('예약39', '2025-04-25', 3, 9),
('예약40', '2025-04-25', 4, 9),
('예약41', '2025-04-25', 5, 9),
('예약42', '2025-04-25', 6, 9),
('예약43', '2025-04-25', 7, 9),
('예약44', '2025-04-25', 8, 9),
('예약45', '2025-04-25', 9, 9),

-- theme_id 10에 10개 예약
('예약46', '2025-04-25', 1, 10),
('예약47', '2025-04-25', 2, 10),
('예약48', '2025-04-25', 3, 10),
('예약49', '2025-04-25', 4, 10),
('예약50', '2025-04-25', 5, 10),
('예약51', '2025-04-25', 6, 10),
('예약52', '2025-04-25', 7, 10),
('예약53', '2025-04-25', 8, 10),
('예약54', '2025-04-25', 9, 10),
('예약55', '2025-04-25', 10, 10),

-- theme_id 11에 11개 예약
('예약56', '2025-04-25', 1, 11),
('예약57', '2025-04-25', 2, 11),
('예약58', '2025-04-25', 3, 11),
('예약59', '2025-04-25', 4, 11),
('예약60', '2025-04-25', 5, 11),
('예약61', '2025-04-25', 6, 11),
('예약62', '2025-04-25', 7, 11),
('예약63', '2025-04-25', 8, 11),
('예약64', '2025-04-25', 9, 11),
('예약65', '2025-04-25', 10, 11),
('예약66', '2025-04-25', 11, 11),

-- theme_id 12에 12개 예약
('예약67', '2025-04-18', 1, 12),
('예약68', '2025-04-18', 2, 12),
('예약69', '2025-04-18', 3, 12),
('예약70', '2025-04-18', 4, 12),
('예약71', '2025-04-18', 5, 12),
('예약72', '2025-04-18', 6, 12),
('예약73', '2025-04-18', 7, 12),
('예약74', '2025-04-18', 8, 12),
('예약75', '2025-04-18', 9, 12),
('예약76', '2025-04-18', 10, 12),
('예약77', '2025-04-18', 11, 12),
('예약78', '2025-04-18', 12, 12),

-- theme_id 13에 13개 예약
('예약79', '2025-04-25', 1, 13),
('예약80', '2025-04-25', 2, 13),
('예약81', '2025-04-25', 3, 13),
('예약82', '2025-04-25', 4, 13),
('예약83', '2025-04-25', 5, 13),
('예약84', '2025-04-25', 6, 13),
('예약85', '2025-04-25', 7, 13),
('예약86', '2025-04-25', 8, 13),
('예약87', '2025-04-25', 9, 13),
('예약88', '2025-04-25', 10, 13),
('예약89', '2025-04-25', 11, 13),
('예약90', '2025-04-25', 12, 13),
('예약91', '2025-04-25', 13, 13);
