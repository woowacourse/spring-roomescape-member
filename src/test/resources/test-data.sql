DELETE
FROM reservation;
DELETE
FROM reservation_time;
DELETE
FROM theme;
DELETE
FROM member;

INSERT INTO member (name, email, password)
VALUES ('유저', 'admin1@email.com', 'password');
