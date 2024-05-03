insert into reservation_time (id, start_at)
values (1, '12:00:00');

insert into theme (id, name, description, thumbnail)
values (1, '테마1', '테마1 설명',
        'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/SpongeBob_SquarePants_character.svg/440px-SpongeBob_SquarePants_character.svg.png');

insert into reservation (id, name, date, time_id, theme_id)
values (1, '아루', '2300-12-24', 1, 1);
