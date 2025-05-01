INSERT INTO reservation_time (start_at)
values ('09:00');
INSERT INTO reservation_time (start_at)
values ('10:00');
INSERT INTO reservation_time (start_at)
values ('11:00');
INSERT INTO reservation_time (start_at)
values ('12:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', 'desc1', 'thumb1');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마2', 'desc2', 'thumb2');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마3', 'desc3', 'thumb3');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마4', 'desc4', 'thumb4');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마5', 'desc5', 'thumb5');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('포비', '2025-1-1', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('포라', '2025-1-2', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('포포', '2025-1-3', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('리사', '2025-1-4', 2, 2);

