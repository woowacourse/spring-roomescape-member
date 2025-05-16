INSERT INTO reservation_time (start_at)
values ('10:00');
INSERT INTO reservation_time (start_at)
values ('11:00');
INSERT INTO reservation_time (start_at)
values ('12:00');
INSERT INTO reservation_time (start_at)
values ('13:00');
INSERT INTO reservation_time (start_at)
values ('14:00');

INSERT INTO theme (name, description, thumbnail)
values ('테마1', '재밌음', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마2', '짱 재밌음', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마3', '개 재밌음', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마4', '헐~~~', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마5', 'ㅋㅋㅋㅋㅋㅋ', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마6', 'ㅎㅎㅎㅎㅎㅎ', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마7', '대박대박짱대박', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마8', '재미없음', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마9', 'siuuuuu', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마10', '메갓', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마11', '역시릅신', 'http://localhost:8080/image/theme.jpg');
INSERT INTO theme (name, description, thumbnail)
values ('테마12', '서커스의신', 'http://localhost:8080/image/theme.jpg');

INSERT INTO member (name, email, password, role)
values ('아마', 'admin@email.com', 'password', 'ADMIN');

INSERT INTO member (name, email, password, role)
values ('제임스 하든', 'harden@email.com', 'harden1', 'MEMBER');

INSERT INTO member (name, email, password, role)
values ('스테판 커리', 'curry@email.com', 'curry1', 'MEMBER');

INSERT INTO member (name, email, password, role)
values ('르브론 제임스', 'james@email.com', 'james1', 'MEMBER');

INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 1, 12, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 2, 12, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 3, 12, 1);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 4, 12, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 5, 11, 2);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 1, 11, 3);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 2, 11, 3);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 3, 10, 3);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 4, 10, 4);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 5, 4, 4);
INSERT INTO reservation (date, time_id, theme_id, member_id)
values ('2025-4-30', 1, 5, 4);


