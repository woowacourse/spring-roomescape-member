DELETE
FROM reservation;
DELETE
FROM reservation_time;
DELETE
FROM theme;
DELETE
FROM member;

INSERT INTO member (role, name, email, password)
VALUES ('ADMIN', '관리자', 'admin1@email.com', 'password');
INSERT INTO member (role, name, email, password)
VALUES ('USER', '유저', 'user1@email.com', 'password');
