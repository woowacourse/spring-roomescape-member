DELETE FROM reservation;
DELETE FROM reservation_time;
DELETE FROM reservation_theme;
DELETE FROM member;

ALTER TABLE member ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation_theme ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;

INSERT INTO member (name, email, password, role)
VALUES ('제프리', 'jeffrey@gmail.com', '1234!@#$', 'USER'),
       ('김지은', 'jieun.kim@example.com', 'abcd!1234', 'USER'),
       ('박철수', 'chulsoo.park@example.com', 'qwer@5678', 'USER'),
       ('이민지', 'minji.lee@example.com', 'zxcv#9012', 'USER'),
       ('관리자', 'admin@gmail.com', '1234!@#$', 'ADMIN');

INSERT INTO reservation_theme (name, description, thumbnail)
VALUES ('레벨 1탈출', '우테코 레벨1를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 2탈출', '우테코 레벨2를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 3탈출', '우테코 레벨3를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 4탈출', '우테코 레벨4를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 5탈출', '우테코 레벨5를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 6탈출', '우테코 레벨6를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 7탈출', '우테코 레벨7를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 8탈출', '우테코 레벨8를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 9탈출', '우테코 레벨9를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 10탈출', '우테코 레벨10를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 11탈출', '우테코 레벨11를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 12탈출', '우테코 레벨12를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('레벨 13탈출', '우테코 레벨13를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation_time (start_at)
VALUES ('15:40'),
       ('16:30'),
       ('17:50'),
       ('18:20'),
       ('19:10');

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -1, CURRENT_DATE), 1, 8, 1),
       (DATEADD('DAY', -1, CURRENT_DATE), 2, 3, 1),
       (DATEADD('DAY', -1, CURRENT_DATE), 3, 11, 1),

       (DATEADD('DAY', -1, CURRENT_DATE), 1, 5, 2),
       (DATEADD('DAY', -1, CURRENT_DATE), 2, 1, 2),
       (DATEADD('DAY', -1, CURRENT_DATE), 3, 12, 2),

       (DATEADD('DAY', -1, CURRENT_DATE), 1, 6, 3),
       (DATEADD('DAY', -1, CURRENT_DATE), 2, 9, 3),
       (DATEADD('DAY', -1, CURRENT_DATE), 3, 2, 3),

       (DATEADD('DAY', -1, CURRENT_DATE), 1, 10, 4),
       (DATEADD('DAY', -1, CURRENT_DATE), 2, 4, 4),
       (DATEADD('DAY', -1, CURRENT_DATE), 3, 7, 4);

