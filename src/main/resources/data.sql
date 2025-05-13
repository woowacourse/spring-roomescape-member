-- 테마 등록
insert into theme (name, description, thumbnail)
values ('theme1', 'description', 'thumbnail'),
       ('theme2', 'description', 'thumbnail'),
       ('theme3', 'description', 'thumbnail'),
       ('theme4', 'description', 'thumbnail'),
       ('theme5', 'description', 'thumbnail'),
       ('theme6', 'description', 'thumbnail'),
       ('theme7', 'description', 'thumbnail'),
       ('theme8', 'description', 'thumbnail'),
       ('theme9', 'description', 'thumbnail'),
       ('theme10', 'description', 'thumbnail'),
       ('theme11', 'description', 'thumbnail'),
       ('theme12', 'description', 'thumbnail'),
       ('theme13', 'description', 'thumbnail')
;

-- 사용자
insert into member (name, email, password, role)
values ('name1', 'email1@email.com', 'password', 'USER'),
       ('name2', 'email2@email.com', 'password', 'USER'),
       ('name3', 'email3@email.com', 'password', 'USER'),
       ('name4', 'email4@email.com', 'password', 'ADMIN'),
       ('name5', 'email5@email.com', 'password', 'ADMIN'),
       ('name6', 'email6@email.com', 'password', 'ADMIN'),
       ('name7', 'email7@email.com', 'password', 'ADMIN'),
       ('name8', 'email8@email.com', 'password', 'ADMIN'),
       ('name9', 'email9@email.com', 'password', 'ADMIN'),
       ('name10', 'email10@email.com', 'password', 'ADMIN')
;

insert into reservation_time (start_at)
values ('10:00'),
       ('10:10'),
       ('10:20'),
       ('10:30'),
       ('10:40'),
       ('10:50'),
       ('11:00'),
       ('11:10'),
       ('11:20'),
       ('11:30'),
       ('11:40'),
       ('11:50'),
       ('12:00'),
       ('12:10'),
       ('12:20'),
       ('12:30'),
       ('12:40'),
       ('12:50'),
       ('13:00'),
       ('13:10'),
       ('13:20'),
       ('13:30'),
       ('13:40'),
       ('13:50'),
       ('14:00'),
       ('14:10'),
       ('14:20'),
       ('14:30'),
       ('14:40'),
       ('14:50'),
       ('15:00'),
       ('15:10'),
       ('15:20'),
       ('15:30'),
       ('15:40'),
       ('15:50'),
       ('16:00'),
       ('16:10'),
       ('16:20'),
       ('16:30'),
       ('16:40'),
       ('16:50'),
       ('17:00'),
       ('17:10'),
       ('17:20'),
       ('17:30'),
       ('17:40'),
       ('17:50'),
       ('18:00'),
       ('18:10'),
       ('18:20'),
       ('18:30'),
       ('18:40'),
       ('18:50'),
       ('19:00'),
       ('19:10'),
       ('19:20'),
       ('19:30'),
       ('19:40'),
       ('19:50'),
       ('20:00'),
       ('20:10'),
       ('20:20'),
       ('20:30');

-- 예약 개수 차등두기
INSERT INTO reservation (member_id, theme_id, date, time_id)
VALUES
-- theme_id = 1 (7일 전)
(1, 1, DATEADD('DAY', -7, CURRENT_DATE), 1),
(2, 1, DATEADD('DAY', -7, CURRENT_DATE), 2),
(3, 1, DATEADD('DAY', -7, CURRENT_DATE), 3),
(4, 1, DATEADD('DAY', -7, CURRENT_DATE), 4),
(5, 1, DATEADD('DAY', -7, CURRENT_DATE), 5),
(6, 1, DATEADD('DAY', -7, CURRENT_DATE), 6),
(7, 1, DATEADD('DAY', -7, CURRENT_DATE), 7),
(8, 1, DATEADD('DAY', -7, CURRENT_DATE), 8),
(9, 1, DATEADD('DAY', -7, CURRENT_DATE), 9),
(10, 1, DATEADD('DAY', -7, CURRENT_DATE), 10),

-- theme_id = 2 (6일 전)
(1, 2, DATEADD('DAY', -6, CURRENT_DATE), 11),
(2, 2, DATEADD('DAY', -6, CURRENT_DATE), 12),
(3, 2, DATEADD('DAY', -6, CURRENT_DATE), 13),
(4, 2, DATEADD('DAY', -6, CURRENT_DATE), 14),
(5, 2, DATEADD('DAY', -6, CURRENT_DATE), 15),
(6, 2, DATEADD('DAY', -6, CURRENT_DATE), 16),
(7, 2, DATEADD('DAY', -6, CURRENT_DATE), 17),
(8, 2, DATEADD('DAY', -6, CURRENT_DATE), 18),
(9, 2, DATEADD('DAY', -6, CURRENT_DATE), 19),

-- theme_id = 3 (5일 전)
(1, 3, DATEADD('DAY', -5, CURRENT_DATE), 20),
(2, 3, DATEADD('DAY', -5, CURRENT_DATE), 21),
(3, 3, DATEADD('DAY', -5, CURRENT_DATE), 22),
(4, 3, DATEADD('DAY', -5, CURRENT_DATE), 23),
(5, 3, DATEADD('DAY', -5, CURRENT_DATE), 24),
(6, 3, DATEADD('DAY', -5, CURRENT_DATE), 25),
(7, 3, DATEADD('DAY', -5, CURRENT_DATE), 26),
(8, 3, DATEADD('DAY', -5, CURRENT_DATE), 27),

-- theme_id = 4 (4일 전)
(1, 4, DATEADD('DAY', -4, CURRENT_DATE), 28),
(2, 4, DATEADD('DAY', -4, CURRENT_DATE), 29),
(3, 4, DATEADD('DAY', -4, CURRENT_DATE), 30),
(4, 4, DATEADD('DAY', -4, CURRENT_DATE), 31),
(5, 4, DATEADD('DAY', -4, CURRENT_DATE), 32),
(6, 4, DATEADD('DAY', -4, CURRENT_DATE), 33),
(7, 4, DATEADD('DAY', -4, CURRENT_DATE), 34),

-- theme_id = 5 (3일 전)
(1, 5, DATEADD('DAY', -3, CURRENT_DATE), 35),
(2, 5, DATEADD('DAY', -3, CURRENT_DATE), 36),
(3, 5, DATEADD('DAY', -3, CURRENT_DATE), 37),
(4, 5, DATEADD('DAY', -3, CURRENT_DATE), 38),
(5, 5, DATEADD('DAY', -3, CURRENT_DATE), 39),
(6, 5, DATEADD('DAY', -3, CURRENT_DATE), 40),

-- theme_id = 6 (2일 전)
(1, 6, DATEADD('DAY', -2, CURRENT_DATE), 41),
(2, 6, DATEADD('DAY', -2, CURRENT_DATE), 42),
(3, 6, DATEADD('DAY', -2, CURRENT_DATE), 43),
(4, 6, DATEADD('DAY', -2, CURRENT_DATE), 44),
(5, 6, DATEADD('DAY', -2, CURRENT_DATE), 45),

-- theme_id = 7 (1일 전)
(1, 7, DATEADD('DAY', -1, CURRENT_DATE), 46),
(2, 7, DATEADD('DAY', -1, CURRENT_DATE), 47),
(3, 7, DATEADD('DAY', -1, CURRENT_DATE), 48),
(4, 7, DATEADD('DAY', -1, CURRENT_DATE), 49),

-- theme_id = 8 (1일 전)
(1, 8, DATEADD('DAY', -1, CURRENT_DATE), 50),
(2, 8, DATEADD('DAY', -1, CURRENT_DATE), 51),
(3, 8, DATEADD('DAY', -1, CURRENT_DATE), 52),

-- theme_id = 9 (1일 전)
(1, 9, DATEADD('DAY', -1, CURRENT_DATE), 53),
(2, 9, DATEADD('DAY', -1, CURRENT_DATE), 54),

-- theme_id = 10 (1일 전)
(1, 10, DATEADD('DAY', -1, CURRENT_DATE), 55);
