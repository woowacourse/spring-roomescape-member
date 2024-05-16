set referential_integrity false;
truncate table reservation restart identity;
truncate table reservation_time restart identity;
truncate table theme restart identity;
truncate table member restart identity;
set referential_integrity true;

INSERT INTO member
values (1, 'admin', 'email', 'password', 'admin');
