insert into reservation_time (id, start_at) values (1, '12:00:00');
insert into reservation_time (id, start_at) values (2, '13:00:00');
insert into reservation_time (id, start_at) values (3, '14:00:00');
insert into reservation_time (id, start_at) values (4, '15:00:00');

insert into theme (id, name, description, thumbnail) values (1, '테마1', '테마1 설명', '테마1 이미지');
insert into theme (id, name, description, thumbnail) values (2, '테마2', '테마2 설명', '테마2 이미지');
insert into theme (id, name, description, thumbnail) values (3, '테마3', '테마3 설명', '테마3 이미지');

insert into reservation (id, name, date, time_id, theme_id) values (1, '아루', '2024-12-25', 1, 1);
