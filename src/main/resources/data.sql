insert into reservation_time (id, start_at) values (1, '12:00');
insert into reservation_time (id, start_at) values (2, '13:00');
insert into reservation_time (id, start_at) values (3, '14:00');

insert into theme (id, name, description, thumbnail) values (1, '테마1', '테마1 입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme (id, name, description, thumbnail) values (2, '테마2', '테마2 입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
insert into theme (id, name, description, thumbnail) values (3, '테마3', '테마3 입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into reservation (id, name, date, time_id, theme_id) values (1, '브라운', '2025-04-29', 1, 3);
insert into reservation (id, name, date, time_id, theme_id) values (2, '브라운', '2025-04-29', 2, 3);
insert into reservation (id, name, date, time_id, theme_id) values (3, '브라운', '2025-04-29', 3, 3);
insert into reservation (id, name, date, time_id, theme_id) values (4, '브라운', '2025-04-29', 1, 1);
insert into reservation (id, name, date, time_id, theme_id) values (5, '브라운', '2025-04-29', 2, 1);
insert into reservation (id, name, date, time_id, theme_id) values (6, '브라운', '2025-04-29', 1, 2);
