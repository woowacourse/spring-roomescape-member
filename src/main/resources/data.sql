insert into reservation_time(start_at) values('15:00');
insert into reservation_time(start_at) values('16:00');
insert into reservation_time(start_at) values('17:00');
insert into reservation_time(start_at) values('18:00');

insert into theme(name, description, thumbnail) values('테스트1', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트2', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트3', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트4', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into reservation(name, date, time_id, theme_id) values('브라운', NOW()-9, 1, 1);
insert into reservation(name, date, time_id, theme_id) values('명오', NOW()-6, 3, 2);
insert into reservation(name, date, time_id, theme_id) values('제제', NOW()-5, 2, 2);
insert into reservation(name, date, time_id, theme_id) values('썬', NOW()-4, 1, 2);
insert into reservation(name, date, time_id, theme_id) values('아서', NOW()-3, 1, 3);
