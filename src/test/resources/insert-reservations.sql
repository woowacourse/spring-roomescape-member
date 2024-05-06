insert into reservation_time (id, start_at)
values (1, '12:00:00'),
       (2, '13:00:00'),
       (3, '14:00:00'),
       (4, '15:00:00');

insert into theme (id, name, description, thumbnail)
values (1, '테마1', '테마1 설명',
        'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/440px-SpongeBob_SquarePants_character.svg.png'),
       (2, '테마2', '테마2 설명',
        'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/440px-SpongeBob_SquarePants_character.svg.png'),
       (3, '테마3', '테마3 설명',
        'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/440px-SpongeBob_SquarePants_character.svg.png'),
       (4, '테마4', '테마4 설명',
        'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/440px-SpongeBob_SquarePants_character.svg.png'),
       (5, '테마5', '테마5 설명',
        'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/440px-SpongeBob_SquarePants_character.svg.png'),
       (6, '테마6', '테마6 설명',
        'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/440px-SpongeBob_SquarePants_character.svg.png');

insert into reservation (id, name, date, time_id, theme_id)
values (1, '아루', '2024-12-24', 1, 2),
       (2, '이상', '2024-12-25', 2, 2),
       (3, '이상', '2024-12-25', 3, 3),
       (4, '이상', '2024-12-25', 2, 3),
       (5, '이상', '2024-12-26', 3, 3),
       (6, '아루', '2024-12-27', 4, 4),
       (7, '수달', '2024-12-28', 1, 4),
       (8, '수달', '2024-12-28', 2, 4),
       (9, '오리', '2024-12-28', 3, 4),
       (10, '오리', '2024-12-28', 4, 5),
       (11, '제이미', '2024-12-29', 1, 1),
       (12, '비밥', '2024-12-29', 2, 1),
       (13, '비밥', '2024-12-29', 3, 1),
       (14, '웨지', '2024-12-29', 4, 1);


values (1, '아루', '2024-4-26', 1, 2),
       (2, '이상', '2024-4-26', 2, 2),
       (3, '이상', '2024-4-26', 3, 3),
       (4, '이상', '2024-4-27', 2, 3),
       (5, '이상', '2024-4-27', 3, 3),
       (6, '아루', '2024-4-28', 4, 4),
       (7, '수달', '2024-4-28', 1, 4),
       (8, '수달', '2024-4-28', 2, 4),
       (9, '오리', '2024-5-1', 3, 4),
       (10, '오리', '2024-5-1', 4, 5),
       (11, '제이미', '2024-5-1', 1, 1),
       (12, '비밥', '2024-5-1', 2, 1),
       (13, '비밥', '2024-5-2', 3, 1),
       (14, '웨지', '2024-5-2', 4, 1);
