INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('13:00'),
       ('14:00'),
       ('15:00');

INSERT INTO theme (name, description, imgUrl)
VALUES ('이든의 공포 하우스', '이든이 귀신으로 나오는 공포 테마', 'https://images.example.com/themes/horror-house.jpg'),
       ('정콩이의 방탈출', '정콩이가 지키는 미스터리 방탈출', 'https://images.example.com/themes/jungkong-room.jpg'),
       ('우주 정거장 탈출', '고장 난 우주 정거장에서 귀환하는 SF 테마', 'https://images.example.com/themes/space-station.jpg');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('브라운', '2026-05-08', 1, 1),
       ('코니', '2026-05-08', 1, 2),
       ('샐리', '2026-05-08', 2, 1);
