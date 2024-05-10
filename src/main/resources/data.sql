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
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨3 탈출',
        '우테코 레벨3를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨4 탈출',
        '우테코 레벨4를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨5 탈출',
        '우테코 레벨5를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨6 탈출',
        '우테코 레벨6를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨7 탈출',
        '우테코 레벨7를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨8 탈출',
        '우테코 레벨8를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨9 탈출',
        '우테코 레벨9를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨10 탈출',
        '우테코 레벨10를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨11 탈출',
        '우테코 레벨11를 탈출하는 내용입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (name, email, password)
VALUES ('어드민', 'admin@production.com', 'password'),
       ('엘라', 'ella@production.com', 'password'),
       ('호돌', 'hodol@production.com', 'password'),
       ('아톰', 'atom@production.com', 'password');

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, DATEADD(DAY, -7, current_date), 1, 8),
       (1, DATEADD(DAY, -6, current_date), 2, 7),
       (3, DATEADD(DAY, -7, current_date), 3, 7),
       (2, current_date, 1, 7),
       (1, current_date, 2, 7),
       (3, DATEADD(DAY, -1, current_date), 2, 7),
       (2, DATEADD(DAY, -1, current_date), 1, 7),
       (1, DATEADD(DAY, -1, current_date), 3, 7),
       (3, DATEADD(DAY, -1, current_date), 2, 6),
       (2, DATEADD(DAY, -1, current_date), 1, 6),
       (1, DATEADD(DAY, -1, current_date), 3, 6),
       (3, DATEADD(DAY, -7, current_date), 2, 6),
       (2, DATEADD(DAY, -7, current_date), 1, 6),
       (1, DATEADD(DAY, -7, current_date), 3, 6),
       (3, DATEADD(DAY, -7, current_date), 2, 5),
       (2, DATEADD(DAY, -7, current_date), 1, 5),
       (1, DATEADD(DAY, -7, current_date), 3, 5),
       (3, DATEADD(DAY, -6, current_date), 2, 5),
       (2, DATEADD(DAY, -6, current_date), 1, 5),
       (1, DATEADD(DAY, -6, current_date), 1, 4),
       (3, DATEADD(DAY, -6, current_date), 2, 4),
       (2, DATEADD(DAY, -1, current_date), 1, 4),
       (1, DATEADD(DAY, -1, current_date), 2, 4),
       (3, DATEADD(DAY, -1, current_date), 2, 3),
       (2, DATEADD(DAY, -1, current_date), 1, 3),
       (1, DATEADD(DAY, -1, current_date), 3, 3),
       (3, DATEADD(DAY, -1, current_date), 2, 1);
