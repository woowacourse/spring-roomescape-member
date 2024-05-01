INSERT INTO theme (id, name, description, thumbnail) VALUES
(1, '테마1', '테마1 설명 테마1 설명', 'thumbnail1.jpg'),
(2, '테마2', '테마2 설명 테마2 설명', 'thumbnail2.jpg'),
(3, '테마3', '테마3 설명 테마3 설명', 'thumbnail3.jpg');


INSERT INTO reservation_time (id, start_at) VALUES
(1,'10:00'),
(2,'12:00'),
(3,'14:00');

INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES
(1,'테드', '2024-04-27', 1, 1),
(2,'종이', '2024-04-30', 2, 2),
(3,'범블비', '2024-05-04', 3, 2),
(4,'제이', '2024-05-05', 3, 2);
