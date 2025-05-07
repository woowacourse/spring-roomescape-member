DELETE FROM reservation;
DELETE FROM reservation_time;
DELETE FROM theme;
DELETE FROM member;

INSERT INTO member(name, email, password, role)
VALUES ('훌라', 'test@test.com', 'pwd', 'USER');
