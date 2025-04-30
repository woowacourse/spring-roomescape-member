INSERT INTO reservation_time(start_at)
VALUES ('10:00');

INSERT INTO reservation_time(start_at)
VALUES ('11:00');

INSERT INTO reservation_time(start_at)
VALUES ('12:00');

INSERT INTO reservation_time(start_at)
VALUES ('13:00');

INSERT INTO reservation_time(start_at)
VALUES ('14:00');

INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 1',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 2',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 3',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 4',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 5',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-28', 1, 1);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-27', 1, 2);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-26', 1, 3);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-25', 1, 3);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-24', 1, 3);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-23', 1, 2);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-22', 1, 4);
