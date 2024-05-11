DELETE
FROM reservation;
ALTER TABLE reservation
    ALTER COLUMN id RESTART;

DELETE
FROM reservation_time;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART;

DELETE
FROM theme;
ALTER TABLE theme
    ALTER COLUMN id RESTART;

DELETE
FROM member;
ALTER TABLE member
    ALTER COLUMN id RESTART;

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('13:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨1 탈출',
        '우테코 레벨1를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨2 탈출',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (name, role, email, password)
VALUES ('어드민', 'ADMIN', 'admin@test.com', 'password');

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2023-05-04', 1, 1),
       (1, '2023-05-04', 2, 1),
       (1, '2023-08-05', 2, 1);
