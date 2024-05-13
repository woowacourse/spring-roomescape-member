INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '설명', '썸네일');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마2', '설명', '썸네일');

INSERT INTO reservation_time (start_at)
VALUES ('10:00');

INSERT INTO member (name, email, password, role)
VALUES ('테니', 'tenny@wooteco.com', '1234', 'MEMBER');

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-10', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-11', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2024-05-01', 1, 2);
