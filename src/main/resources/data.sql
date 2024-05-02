INSERT INTO reservation_time (start_at)
VALUES ('10:00:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('우테코 수료 대장정',
        '수료를 못 할 위기에 놓인 크루를 구하라!',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxwhvlMeM3WyfMxfxvP0QnhK0rTyvWK7cLlkRfMV_EJO3HYsvtdM3FZdXqWN7-T5G0eKg&usqp=CAU'),
       ('현실에선 백수인 내가 이세계에서는 반란군?!',
        '포비의 가르침에 따라 반란군에 합류하고자 하는 크루들은 무사히 취업에 성공할 수 있을까?',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4SY7uJ9j4L4wTd0TWkvCU54ivhSjN_0Wu95x_WXuMLA&s');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('알파카', '2024-06-03', 1, 1),
       ('산초', '2024-06-03', 1, 2),
       ('칸쵸', '2024-06-03', 1, 2);
