INSERT INTO reservation_time (id, start_time, end_time)
VALUES
    (1, '10:00', '11:00'),
    (2, '11:00', '12:00'),
    (3, '12:00', '13:00');

INSERT INTO theme (id, name, description, image_url)
VALUES
    (1, '미궁의 유산', '고대 미궁에서 탈출하세요.', 'https://example.com/themes/1.png'),
    (2, '시간의 균열', '시간이 무너지는 방을 구하세요.', 'https://example.com/themes/2.png'),
    (3, '심해 기지', '심해 기지의 비밀을 밝혀라.', 'https://example.com/themes/3.png');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES
    ('tester1', DATEADD('DAY', -1, CURRENT_DATE), 1, 1),
    ('tester2', DATEADD('DAY', -2, CURRENT_DATE), 2, 1),
    ('tester3', DATEADD('DAY', -3, CURRENT_DATE), 3, 1),

    ('tester4', DATEADD('DAY', -1, CURRENT_DATE), 1, 2),
    ('tester5', DATEADD('DAY', -2, CURRENT_DATE), 2, 2),

    ('tester6', DATEADD('DAY', -1, CURRENT_DATE), 1, 3);
