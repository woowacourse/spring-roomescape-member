insert into member(name, role, email, password)
values ('명오', 'ADMIN', 'hkim1109@naver.com', 'qwer1234');
insert into member(name, role, email, password)
values ('폰드', 'NORMAL', 'tack0913@naver.com', 'asdf1234');
insert into member(name, role, email, password)
values ('제제', 'NORMAL', 'jinwoo22@gmail.com', '1q2w3e4r');

insert into reservation_time(start_at)
values ('15:00');
insert into reservation_time(start_at)
values ('16:00');
insert into reservation_time(start_at)
values ('17:00');
insert into reservation_time(start_at)
values ('18:00');

insert into theme(name, description, thumbnail)
values ('테스트1', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail)
values ('테스트2', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail)
values ('테스트3', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme(name, description, thumbnail)
values ('테스트4', '테스트중', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into reservation(date, member_id, time_id, theme_id)
values (NOW() - 9, 1, 1, 1);
insert into reservation(date, member_id, time_id, theme_id)
values (NOW() - 6, 2, 3, 2);
insert into reservation(date, member_id, time_id, theme_id)
values (NOW() - 5, 2, 2, 2);
insert into reservation(date, member_id, time_id, theme_id)
values (NOW() - 4, 3, 1, 2);
insert into reservation(date, member_id, time_id, theme_id)
values (NOW() - 3, 1, 1, 3);
