INSERT INTO reservation_time(start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00');

INSERT INTO theme(theme_name, description, thumbnail)
VALUES ('예시 1',
        '테스트 용 테마 1번 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('예시 2',
        '테스트 용 테마 2번 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('예시 3',
        '테스트 용 테마 3번 입니다.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member(role, name, email, password)
VALUES ('ADMIN', '어드민', 'admin@email.com', 'password'),
       ('USER', '사용자', 'user@email.com', 'password');

INSERT INTO reservation(date, time_id, theme_id, member_id)
VALUES ('2025-04-26', 1, 1, 1),
       ('2025-04-27', 1, 2, 1),
       ('2025-04-28', 1, 1, 1);
