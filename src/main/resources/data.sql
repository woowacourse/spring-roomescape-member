insert into reservation_time(start_at) values('15:00');
insert into reservation_time(start_at) values('16:00');
insert into reservation_time(start_at) values('17:00');
insert into reservation_time(start_at) values('18:00');

insert into theme(name, description, thumbnail) values('테스트1', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트2', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트3', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail) values('테스트4', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into member(name, email, password) values('명오', 'myoungO@gmail.com', 'myoungO');
insert into member(name, email, password) values ('제제', 'zeze@gmail.com', 'zeze');
insert into member(name, email, password) values('썬', 'myoungO@gmail.com', 'sun');
insert into member(name, email, password) values('아서', 'hyunta@gmail.com', 'hyunta');
insert into member(name, email, password) values('솔라', 'sola@gmail.com', 'sola');

insert into reservation(member_id, date, time_id, theme_id) values(1, '2024-04-25', 1, 1);
insert into reservation(member_id, date, time_id, theme_id) values(2, '2024-04-26', 3, 2);
insert into reservation(member_id, date, time_id, theme_id) values(3, '2024-04-26', 2, 2);
insert into reservation(member_id, date, time_id, theme_id) values(4, '2024-05-01', 1, 2);
insert into reservation(member_id, date, time_id, theme_id) values(5, '2024-05-01', 1, 3);

