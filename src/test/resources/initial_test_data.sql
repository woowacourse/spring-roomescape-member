INSERT INTO reservation_time (start_at)
VALUES ('09:00');

INSERT INTO reservation_time (start_at)
VALUES ('10:00');

INSERT INTO reservation_time (start_at)
VALUES ('11:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨1 탈출', '우테코 레벨1를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨3 탈출', '우테코 레벨3를 탈출하는 내용입니다.', '아무 내용 없음');

INSERT INTO member (name, email, password)
VALUES ('카고', 'kargo123@email.com', '123456');

INSERT INTO member (name, email, password)
VALUES ('브라운', 'brown123@email.com', '123456');

INSERT INTO member (name, email, password)
VALUES ('솔라', 'solar123@email.com', '123456');

INSERT INTO member (name, email, password)
VALUES ('어드민', 'admin@email.com', 'password');

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-25', 1, 1, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2099-05-01', 1, 1, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-25', 2, 1, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-25', 2, 2, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-27', 1, 2, 1);

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-04-27', 2, 2, 1);
