DELETE
FROM reservation;
DELETE
FROM reservation_time;
DELETE
FROM theme;

INSERT INTO users (name, email, password)
VALUES ('유저', 'admin@email.com', 'password');
