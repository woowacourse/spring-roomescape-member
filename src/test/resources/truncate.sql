set referential_integrity false;
truncate table reservation;
truncate table reservation_time;
truncate table theme;
truncate table member;
set referential_integrity true;

INSERT INTO member
values (1, 'admin', 'email', 'password', 'admin');
