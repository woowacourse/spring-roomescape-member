create table reservation_time
(
    id       bigint not null auto_increment,
    start_at time   not null,
    primary key (id)
);

create table reservation
(
    id      bigint       not null auto_increment,
    name    varchar(255) not null,
    date    date         not null,
    time_id bigint       not null,
    primary key (id),
    foreign key (time_id) references reservation_time (id)
);
