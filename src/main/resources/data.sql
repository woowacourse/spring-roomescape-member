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
VALUES ('루키', 'rookie@woowa.com', '$2a$10$gUl.BwXPlmTJ3atyx1GIJeN725yqEu.WXlAL9HTub6GKGCl14yiS.', 'USER');
INSERT INTO member (name, email, password, role)
VALUES ('하루', 'haru@woowa.com', '$2a$10$XqqZ2oRlZn/VkOq5U2TKDedp.fe.uM.HiHF9G9oqxV7P9HDlSlPQ2', 'USER');
INSERT INTO member (name, email, password, role)
VALUES ('베루스', 'verus@woowa.com', '$2a$10$ysmXXi.Eoez4lMARiwG25.XCJM9qQl4k7O.aRP.K1/Ax9edEY2zVa', 'ADMIN');
INSERT INTO member (name, email, password, role)
VALUES ('사용자', 'user@user.com', '$2a$10$hCNTGzfIFwlVySRa0cv0buTCl9camwNX4k179h7sy32EJycXisJzm', 'USER');
INSERT INTO member (name, email, password, role)
VALUES ('관리자', 'admin@admin.com', '$2a$10$/tUyrkwviznsJbm3p/LHQ.qgksOmR0Fz0rbMk5wQLmakZt84ucNJq', 'ADMIN');

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
