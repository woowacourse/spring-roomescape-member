INSERT INTO reservation_time (start_at)
VALUES
    ('10:00'),
    ('11:00');

INSERT INTO reservation (name, date, time_id)
VALUES
    ('브라운', '2024-04-01', 1),
    ('솔라',   '2024-04-01', 2),
    ('브리',   '2024-04-02', 1);

INSERT INTO theme (name, description, thumbnail)
VALUES
    ('테마1', '테마 1입니다', '썸네일1'),
    ('테마2', '테마 2입니다', '썸네일2');
