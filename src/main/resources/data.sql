insert into theme(name, description, thumbnail)
values ('테마1', '테마1입니당 ^0^', 'https://file.miricanvas.com/template_thumb/2021/07/02/13/20/k4t92g5ntu46etia/thumb.jpg');

insert into theme(name, description, thumbnail)
values ('테마2', '테마2입니당 ^0^', 'https://file.miricanvas.com/template_thumb/2021/07/02/13/20/k4t92g5ntu46etia/thumb.jpg');

insert into theme(name, description, thumbnail)
values ('테마3', '테마3입니당 ^0^', 'https://file.miricanvas.com/template_thumb/2021/07/02/13/20/k4t92g5ntu46etia/thumb.jpg');

insert into reservation_time(start_at)
values ('15:00:00');

insert into reservation_time(start_at)
values ('16:00:00');

insert into reservation_time(start_at)
values ('17:00:00');

insert into member(name, email, password, role)
values ('썬', 'aa@gmail.com', '123', 'MEMBER'),
       ('리비', 'bb@gmail.com', '123', 'MEMBER'),
       ('도도', 'cc@gmail.com', '123', 'MEMBER'),
       ('어드민', 'dd@gmail.com', '123', 'ADMIN');


insert into reservation(member_id, date, time_id, theme_id)
values (1, '2024-05-01', '1', '1');

insert into reservation(member_id, date, time_id, theme_id)
values (2, '2024-05-01', '2', '1');

insert into reservation(member_id, date, time_id, theme_id)
values (1, '2024-05-01', '3', '1');

insert into reservation(member_id, date, time_id, theme_id)
values (3, '2024-04-28', '1', '2');

insert into reservation(member_id, date, time_id, theme_id)
values (2, '2024-04-28', '2', '2');

