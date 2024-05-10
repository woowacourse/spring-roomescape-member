create table member
(
    id       bigint       not null auto_increment,
    name     varchar(255) not null,
    email    varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

insert into member (id, name, email, password)
values (1, '피케이', 'pkpkpkpk@woowa.net', 'anything'),
       (2, '망쵸', 'mangcho@woowa.net', 'nothing');
