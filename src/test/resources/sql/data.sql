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

INSERT INTO member (name, email, password, role)
VALUES ('포비', 'email1@domain.com', 'password1', 'MEMBER');
INSERT INTO member (name, email, password, role)
VALUES ('포라', 'email2@domain.com', 'password2', 'MEMBER');
INSERT INTO member (name, email, password, role)
VALUES ('포포', 'email3@domain.com', 'password3', 'MEMBER');
INSERT INTO member (name, email, password, role)
VALUES ('리사', 'email4@domain.com', 'password4', 'MEMBER');
INSERT INTO member (name, email, password, role)
VALUES ('admin', 'admin@domain.com', 'admin', 'ADMIN');


INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2025-1-1', 1, 1);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2025-1-2', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (3, '2025-1-3', 2, 1);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (4, '2025-1-4', 2, 2);

