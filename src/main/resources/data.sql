insert into reservation_time(start_at) values('15:00');
insert into reservation_time(start_at) values('16:00');
insert into reservation_time(start_at) values('17:00');
insert into reservation_time(start_at) values('18:00');

insert into theme(name, description, thumbnail) values('테스트1', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트2', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트3', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트4', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into reservation(name, date, time_id, theme_id) values('브라운', '2024-04-25', 1, 1);
insert into reservation(name, date, time_id, theme_id) values('명오', '2024-04-26', 3, 2);
insert into reservation(name, date, time_id, theme_id) values('제제', '2024-04-26', 2, 2);
insert into reservation(name, date, time_id, theme_id) values('썬', '2024-05-01', 1, 2);
insert into reservation(name, date, time_id, theme_id) values('아서', '2024-05-01', 1, 3);

insert into member(name, email, password) values ('제제', 'zeze@gmail.com', 'zeze');
