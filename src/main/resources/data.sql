INSERT INTO reservation_time (start_at)
VALUES ('00:00');
INSERT INTO reservation_time (start_at)
VALUES ('10:10');
INSERT INTO reservation_time (start_at)
VALUES ('12:00');
INSERT INTO reservation_time (start_at)
VALUES ('15:30');
INSERT INTO reservation_time (start_at)
VALUES ('18:00');
INSERT INTO reservation_time (start_at)
VALUES ('20:30');

INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨1 탈출', '우테코 레벨1를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨3 탈출', '우테코 레벨3를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨4 탈출', '우테코 레벨4를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨5 탈출', '우테코 레벨5를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨6 탈출', '우테코 레벨6를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (name, email, password, role)
VALUES ('루키', 'rookie@woowa.com', 'rookie123', 'USER');
INSERT INTO member (name, email, password, role)
VALUES ('하루', 'haru@woowa.com', 'haru123', 'USER');
INSERT INTO member (name, email, password, role)
VALUES ('베루스', 'verus@woowa.com', 'verus123', 'ADMIN');
INSERT INTO member (name, email, password, role)
VALUES ('사용자', 'user@user.com', 'user123', 'USER');
INSERT INTO member (name, email, password, role)
VALUES ('관리자', 'admin@admin.com', 'admin123', 'ADMIN');

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -6, CURRENT_DATE), 1, 1, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -5, CURRENT_DATE), 2, 1, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -5, CURRENT_DATE), 3, 1, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -4, CURRENT_DATE), 4, 2, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -4, CURRENT_DATE), 1, 2, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -3, CURRENT_DATE), 2, 2, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -3, CURRENT_DATE), 3, 3, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -2, CURRENT_DATE), 4, 3, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', -1, CURRENT_DATE), 1, 3, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (CURRENT_DATE, 2, 3, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', +1, CURRENT_DATE), 3, 4, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', +2, CURRENT_DATE), 4, 4, 3);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', +3, CURRENT_DATE), 1, 5, 3);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', +4, CURRENT_DATE), 2, 5, 3);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', +4, CURRENT_DATE), 3, 5, 3);
INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES (DATEADD('DAY', +5, CURRENT_DATE), 4, 5, 3);
