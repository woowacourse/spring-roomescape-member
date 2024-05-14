DELETE FROM reservation;
ALTER TABLE reservation ALTER COLUMN id RESTART;

DELETE  FROM member;
ALTER TABLE member ALTER COLUMN id RESTART;

INSERT INTO member (name, email, password, role)
VALUES ('user1', 'user1@gmail.com', 'user1','USER'),
       ('user2', 'user2@gmail.com','user2','USER'),
       ('admin', 'admin@gmail.com', 'admin','ADMIN');
