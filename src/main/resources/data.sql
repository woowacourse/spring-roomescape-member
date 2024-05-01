INSERT INTO theme (name, description, thumbnail) VALUES
('테마1', '테마1 설명 테마1 설명', 'thumbnail1.jpg'),
('테마2', '테마2 설명 테마2 설명', 'thumbnail2.jpg'),
('테마3', '테마3 설명 테마3 설명', 'thumbnail3.jpg');


INSERT INTO reservation_time (start_at) VALUES
('10:00'),
('12:00'),
('14:00');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES
(`'테드', '2024-04-27', 1, 1),
(`'종이', '2024-04-30', 2, 2),
(`'범블비', '2024-05-04', 3, 2),
(`'제이', '2024-05-05', 3, 2);
