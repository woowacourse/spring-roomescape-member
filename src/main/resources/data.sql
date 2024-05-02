INSERT INTO theme(name, description, thumbnail)
VALUES ('테마1', '설명1', '썸네일1');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마2', '설명2', '썸네일2');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마3', '설명3', '썸네일3');

INSERT INTO reservation_time(start_at)
VALUES ('11:59');
INSERT INTO reservation_time(start_at)
VALUES ('17:12');
INSERT INTO reservation_time(start_at)
VALUES ('11:11');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('예약1', '2025-01-25', 1, 1);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('예약2', '2025-01-26', 2, 2);
INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('예약3', '2024-05-09', 3, 2);

INSERT INTO member(name)
VALUES ('choco');

INSERT INTO reservation_list(member_id, reservation_id)
VALUES (1, 1);

