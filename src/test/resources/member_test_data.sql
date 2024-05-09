DELETE FROM reservation;
ALTER TABLE reservation ALTER COLUMN id RESTART;

DELETE  FROM member;
ALTER TABLE member ALTER COLUMN id RESTART;

INSERT INTO member (name, email, password) VALUES ('test', 'test@gmail.com', 'test');
