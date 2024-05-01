create table theme
(
    id          bigint       not null auto_increment,
    name        varchar(255) not null,
    description varchar(255) not null,
    thumbnail   varchar(255) not null,
    primary key (id)
);

create table reservation_time
(
    id       bigint not null auto_increment,
    start_at time   not null,
    primary key (id)
);

create table reservation
(
    id       bigint       not null auto_increment,
    name     varchar(255) not null,
    date     date         not null,
    time_id  bigint       not null,
    theme_id bigint       not null,
    primary key (id),
    foreign key (time_id) references reservation_time (id),
    foreign key (theme_id) references theme (id)
);
--
-- insert into theme (name, description, thumbnail) values ('Theme 1', 'Description 1', 'thumbnail1.jpg');
-- insert into reservation_time (start_at) values ('10:00:00');
