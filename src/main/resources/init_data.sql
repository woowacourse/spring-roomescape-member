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
('예약1', '2024-07-01', 1, 1),

-- theme_id 2에 2개 예약
('예약2', '2024-07-02', 1, 2),
('예약3', '2024-07-03', 2, 2),

-- theme_id 3에 3개 예약
('예약4', '2024-07-04', 1, 3),
('예약5', '2024-07-05', 2, 3),
('예약6', '2024-07-06', 3, 3),

-- theme_id 4에 4개 예약
('예약7', '2024-07-07', 1, 4),
('예약8', '2024-07-08', 2, 4),
('예약9', '2024-07-09', 3, 4),
('예약10', '2024-07-10', 4, 4),

-- theme_id 5에 5개 예약
('예약11', '2024-07-11', 1, 5),
('예약12', '2024-07-12', 2, 5),
('예약13', '2024-07-13', 3, 5),
('예약14', '2024-07-14', 4, 5),
('예약15', '2024-07-15', 5, 5),

-- theme_id 6에 6개 예약
('예약16', '2024-07-16', 1, 6),
('예약17', '2024-07-17', 2, 6),
('예약18', '2024-07-18', 3, 6),
('예약19', '2024-07-19', 4, 6),
('예약20', '2024-07-20', 5, 6),
('예약21', '2024-07-21', 6, 6),

-- theme_id 7에 7개 예약
('예약22', '2024-07-22', 1, 7),
('예약23', '2024-07-23', 2, 7),
('예약24', '2024-07-24', 3, 7),
('예약25', '2024-07-25', 4, 7),
('예약26', '2024-07-26', 5, 7),
('예약27', '2024-07-27', 6, 7),
('예약28', '2024-07-28', 7, 7),

-- theme_id 8에 8개 예약
('예약29', '2024-07-29', 1, 8),
('예약30', '2024-07-30', 2, 8),
('예약31', '2024-07-31', 3, 8),
('예약32', '2024-08-01', 4, 8),
('예약33', '2024-08-02', 5, 8),
('예약34', '2024-08-03', 6, 8),
('예약35', '2024-08-04', 7, 8),
('예약36', '2024-08-05', 8, 8),

-- theme_id 9에 9개 예약
('예약37', '2024-08-06', 1, 9),
('예약38', '2024-08-07', 2, 9),
('예약39', '2024-08-08', 3, 9),
('예약40', '2024-08-09', 4, 9),
('예약41', '2024-08-10', 5, 9),
('예약42', '2024-08-11', 6, 9),
('예약43', '2024-08-12', 7, 9),
('예약44', '2024-08-13', 8, 9),
('예약45', '2024-08-14', 9, 9),

-- theme_id 10에 10개 예약
('예약46', '2024-08-15', 1, 10),
('예약47', '2024-08-16', 2, 10),
('예약48', '2024-08-17', 3, 10),
('예약49', '2024-08-18', 4, 10),
('예약50', '2024-08-19', 5, 10),
('예약51', '2024-08-20', 6, 10),
('예약52', '2024-08-21', 7, 10),
('예약53', '2024-08-22', 8, 10),
('예약54', '2024-08-23', 9, 10),
('예약55', '2024-08-24', 10, 10),

-- theme_id 11에 11개 예약
('예약56', '2024-08-25', 1, 11),
('예약57', '2024-08-26', 2, 11),
('예약58', '2024-08-27', 3, 11),
('예약59', '2024-08-28', 4, 11),
('예약60', '2024-08-29', 5, 11),
('예약61', '2024-08-30', 6, 11),
('예약62', '2024-08-31', 7, 11),
('예약63', '2024-09-01', 8, 11),
('예약64', '2024-09-02', 9, 11),
('예약65', '2024-09-03', 10, 11),
('예약66', '2024-09-04', 11, 11),

-- theme_id 12에 12개 예약
('예약67', '2024-09-05', 1, 12),
('예약68', '2024-09-06', 2, 12),
('예약69', '2024-09-07', 3, 12),
('예약70', '2024-09-08', 4, 12),
('예약71', '2024-09-09', 5, 12),
('예약72', '2024-09-10', 6, 12),
('예약73', '2024-09-11', 7, 12),
('예약74', '2024-09-12', 8, 12),
('예약75', '2024-09-13', 9, 12),
('예약76', '2024-09-14', 10, 12),
('예약77', '2024-09-15', 11, 12),
('예약78', '2024-09-16', 12, 12),

-- theme_id 13에 13개 예약
('예약79', '2024-09-17', 1, 13),
('예약80', '2024-09-18', 2, 13),
('예약81', '2024-09-19', 3, 13),
('예약82', '2024-09-20', 4, 13),
('예약83', '2024-09-21', 5, 13),
('예약84', '2024-09-22', 6, 13),
('예약85', '2024-09-23', 7, 13),
('예약86', '2024-09-24', 8, 13),
('예약87', '2024-09-25', 9, 13),
('예약88', '2024-09-26', 10, 13),
('예약89', '2024-09-27', 11, 13),
('예약90', '2024-09-28', 12, 13),
('예약91', '2024-09-29', 13, 13);
