INSERT INTO reservation_time (id, start_at)
VALUES (10, '09:00');

INSERT INTO reservation_time (id, start_at)
VALUES (11, '10:00');

INSERT INTO reservation_time (id, start_at)
VALUES (12, '11:00');

INSERT INTO theme (id, name, description, thumbnail)
VALUES (10, '레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO theme (id, name, description, thumbnail)
VALUES (11, '레벨3 탈출', '우테코 레벨3를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (10, '브라운', '2024-04-25', 10, 10);

INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (11, '솔라', '2099-05-01', 12, 10);

