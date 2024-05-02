insert into reservation_time(start_at) values('15:00');
insert into reservation_time(start_at) values('16:00');
insert into reservation_time(start_at) values('17:00');
insert into reservation_time(start_at) values('18:00');

insert into theme(name, description, thumbnail) values('테스트1', '테스트중', 'https://test');
insert into theme(name, description, thumbnail) values('테스트2', '테스트중', 'https://test');
insert into theme(name, description, thumbnail) values('테스트3', '테스트중', 'https://test');
insert into theme(name, description, thumbnail) values('테스트4', '테스트중', 'https://test');

insert into reservation(name, date, time_id, theme_id) values('브라운', '2024-04-25', 1, 1);
insert into reservation(name, date, time_id, theme_id) values('명오', '2024-04-26', 3, 2);
insert into reservation(name, date, time_id, theme_id) values('제제', '2024-04-26', 2, 2);
insert into reservation(name, date, time_id, theme_id) values('누구', '2024-05-01', 1, 2);
insert into reservation(name, date, time_id, theme_id) values('누구세요', '2024-05-01', 1, 3);
