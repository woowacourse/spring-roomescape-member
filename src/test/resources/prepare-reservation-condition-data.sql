SET
REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE reservation;
ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE reservation_time;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE theme;
ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE members;
ALTER TABLE members
    ALTER COLUMN id RESTART WITH 1;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '테마 1입니다', 'thumbnail.jpg'),
       ('테마2', '테마 2입니다', 'thumbnail.jpg'),
       ('테마3', '테마 3입니다', 'thumbnail.jpg'),
       ('테마4', '테마 4입니다', 'thumbnail.jpg'),
       ('테마5', '테마 5입니다', 'thumbnail.jpg'),
       ('테마6', '테마 6입니다', 'thumbnail.jpg'),
       ('테마7', '테마 7입니다', 'thumbnail.jpg'),
       ('테마8', '테마 8입니다', 'thumbnail.jpg'),
       ('테마9', '테마 9입니다', 'thumbnail.jpg'),
       ('테마10', '테마 10입니다', 'thumbnail.jpg'),
       ('테마11', '테마 11입니다', 'thumbnail.jpg');

INSERT INTO members(email, password, name, role)
VALUES ('test@email.com', 'password', '멍구', 'ADMIN'),
       ('test2@email.com', 'password', '아이나', 'USER');
