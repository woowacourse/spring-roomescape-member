INSERT INTO reservation_time(start_at) VALUES
('10:00'),
('12:00');

INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 1',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('test', '2025-04-28', 1, 1);
