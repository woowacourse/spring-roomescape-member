INSERT INTO RESERVATION_TIME (START_AT)
VALUES
    ('10:00'),
    ('12:00'),
    ('14:00');

INSERT INTO THEME (NAME, DESCRIPTION, THUMBNAIL)
VALUES
    ('레벨1', '설명1', '썸네일1'),
    ('레벨2', '설명2', '썸네일2'),
    ('레벨3', '설명3', '썸네일3');

INSERT INTO RESERVATION (NAME, DATE, TIME_ID, THEME_ID)
VALUES
    ('예약1', '2025-04-24', 1, 2),
    ('예약2', '2025-04-24', 2, 2),
    ('예약3', '2025-04-24', 3, 2),
    ('예약4', '2025-04-24', 1, 1),
    ('예약5', '2025-04-25', 2, 3),
    ('예약6', '2025-04-25', 3, 3);
