INSERT INTO times(start_at)
values ('10:00');
INSERT INTO times(start_at)
values ('12:00');
INSERT INTO times(start_at)
values ('14:00');
INSERT INTO times(start_at)
values ('16:00');
INSERT INTO times(start_at)
values ('18:00');
INSERT INTO times(start_at)
values ('20:00');

INSERT INTO themes(name, thumbnail_url, description)
VALUES ('냥이 점집', 'https://i.postimg.cc/3JRp43dK/1553676990.jpg', '난이도 NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('어느 구두쇠의 전시회장', 'https://i.postimg.cc/4yrMrRfQ/image.jpg', '난이도: NORMAL 3/5');


INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('티온', '2026-05-10', 1, 1);
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('달수', '2026-05-10', 2, 2)
