DELETE FROM reservation
DELETE FROM reservation_time
DELETE FROM theme

INSERT INTO member (name, email, password) VALUES ('어드민', 'user@example.com', 'password1!');
