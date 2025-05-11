-- 예약 시간 관련

insert into reservation_time (start_at)
values ('12:00');

insert into reservation_time (start_at)
values ('13:00');

insert into reservation_time (start_at)
values ('14:00');

-- 테마 관련

insert into theme (name, description, thumbnail)
values ('테마1', '테마1 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into theme (name, description, thumbnail)
values ('테마2', '테마2 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

insert into theme (name, description, thumbnail)
values ('테마3', '테마3 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

-- 유저 관련

insert into users (email, name, password, role)
values ('admin@naver.com', '어드민',
        '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'ROLE_ADMIN');

insert into users (email, name, password, role)
values ('brown@naver.com', '브라운',
        '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'ROLE_USER');

insert into users (email, name, password, role)
values ('kkuk@naver.com', '꾹이', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        'ROLE_USER');

-- 예약 관련

insert into reservation (user_id, date, time_id, theme_id)
values (2, '2025-04-29', 1, 3);

insert into reservation (user_id, date, time_id, theme_id)
values (2, '2025-04-29', 2, 3);

insert into reservation (user_id, date, time_id, theme_id)
values (2, '2025-04-29', 3, 3);

insert into reservation (user_id, date, time_id, theme_id)
values (2, '2025-04-29', 2, 1);

insert into reservation (user_id, date, time_id, theme_id)
values (3, '2025-04-30', 1, 2);

insert into reservation (user_id, date, time_id, theme_id)
values (3, '2025-04-30', 1, 1);


