INSERT INTO theme(id, name, description, thumbnail) VALUES (1, '테마1', '설명1', '썸네일1');
INSERT INTO theme(id, name, description, thumbnail) VALUES (2, '테마2', '설명2', '썸네일2');
INSERT INTO theme(id, name, description, thumbnail) VALUES (3, '테마3', '설명3', '썸네일3');

INSERT INTO reservation_time(id, start_at) VALUES (1, '11:59');
INSERT INTO reservation_time(id, start_at) VALUES (2, '17:12');
INSERT INTO reservation_time(id, start_at) VALUES (3, '11:11');

INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES (1, '예약1', '2025-01-25', 1, 1);
INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES (2, '예약2', '2025-01-26', 2, 2);
INSERT INTO reservation(id, name, date, time_id, theme_id) VALUES (3, '예약3', '2025-01-27', 3, 2);

INSERT INTO member(id, name) VALUES (1, 'choco');

INSERT INTO reservation_list(id, member_id, reservation_id) VALUES (1, 1, 1);

