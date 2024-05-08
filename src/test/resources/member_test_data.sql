DELETE  FROM member;
ALTER TABLE member ALTER COLUMN id RESTART;

INSERT INTO member (name, email, password) VALUES ('test', 'test@gmail.com', 'test');
