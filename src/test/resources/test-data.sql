DELETE
FROM reservation;
DELETE
FROM reservation_time;
DELETE
FROM theme;
DELETE
FROM users;

INSERT INTO users (name, email, password)
VALUES ('유저', 'admin1@email.com', 'password');
