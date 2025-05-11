insert into reservation_time (start_at)
values ('12:00');
insert into reservation_time (start_at)
values ('13:00');
insert into reservation_time (start_at)
values ('14:00');

insert into theme (name, description, thumbnail)
values ('테마1', '테마1 입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme (name, description, thumbnail)
values ('테마2', '테마2 입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme (name, description, thumbnail)
values ('테마3', '테마3 입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into reservation (name, date, time_id, theme_id)
values ('브라운', '2025-04-29', 1, 3);
insert into reservation (name, date, time_id, theme_id)
values ('브라운', '2025-04-29', 2, 3);
insert into reservation (name, date, time_id, theme_id)
values ('브라운', '2025-04-29', 3, 3);
insert into reservation (name, date, time_id, theme_id)
values ('브라운', '2025-04-29', 1, 1);
insert into reservation (name, date, time_id, theme_id)
values ('브라운', '2025-04-29', 2, 1);
insert into reservation (name, date, time_id, theme_id)
values ('브라운', '2025-04-29', 1, 2);

insert into member (name, email, password)
values ('포스티', 'posty@woowa.com', '12345678');
