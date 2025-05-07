INSERT INTO reservation_time (start_at)
values ('09:00');
INSERT INTO reservation_time (start_at)
values ('10:00');
INSERT INTO reservation_time (start_at)
values ('11:00');
INSERT INTO reservation_time (start_at)
values ('12:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', 'description1', 'thumb1.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마2', 'description2', 'thumb2.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마3', 'description3', 'thumb3.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마4', 'description4', 'thumb4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마5', 'description5', 'thumb5.jpg');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('포비', '2025-1-1', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('포라', '2025-1-2', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('포포', '2025-1-3', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('리사', '2025-1-4', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('브라운', '2025-1-4', 3, 2);

