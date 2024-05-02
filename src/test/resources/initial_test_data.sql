INSERT INTO reservation_time (start_at)
VALUES ('09:00');

INSERT INTO reservation_time (start_at)
VALUES ('10:00');

INSERT INTO reservation_time (start_at)
VALUES ('11:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨3 탈출', '우테코 레벨3를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('브라운', '2024-04-25', 1, 1);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('솔라', '2099-05-01', 1, 1);

