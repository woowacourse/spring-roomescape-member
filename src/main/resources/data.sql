INSERT INTO reservation_time(start_at)
VALUES ('10:00');
INSERT INTO reservation_time(start_at)
VALUES ('11:00');
INSERT INTO reservation_time(start_at)
VALUES ('12:00');

INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 1',
        '테스트 용 테마 1번 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 2',
        '테스트 용 테마 2번 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 3',
        '테스트 용 테마 3번 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test1', '2025-04-26', 1, 1);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test2', '2025-04-27', 1, 2);

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test3', '2025-04-28', 1, 1);
