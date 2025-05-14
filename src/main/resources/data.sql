-- 예약 시간
INSERT INTO reservation_time (start_at)
VALUES ('10:00');
INSERT INTO reservation_time (start_at)
VALUES ('11:30');
INSERT INTO reservation_time (start_at)
VALUES ('13:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:30');
INSERT INTO reservation_time (start_at)
VALUES ('16:00');
INSERT INTO reservation_time (start_at)
VALUES ('17:30');
INSERT INTO reservation_time (start_at)
VALUES ('19:00');
INSERT INTO reservation_time (start_at)
VALUES ('20:30');

-- 테마
INSERT INTO theme (name, description, thumbnail)
VALUES ('공포의 저택', '무서운 분위기의 탈출 게임', 'https://ibb.co/LX9kvnB0');
INSERT INTO theme (name, description, thumbnail)
VALUES ('미래 도시', 'SF 컨셉의 퍼즐 탈출', 'https://ibb.co/nMfdprVX');
INSERT INTO theme (name, description, thumbnail)
VALUES ('탐정 사무소', '추리력을 시험하는 사건 해결', 'https://ibb.co/ksW1qjQr');
INSERT INTO theme (name, description, thumbnail)
VALUES ('마법 학교', '마법 세계를 배경으로 한 탈출', 'https://ibb.co/fR41HgN');
INSERT INTO theme (name, description, thumbnail)
VALUES ('우주 탈출', '우주 정거장에서 벌어지는 스릴러', 'https://ibb.co/example01');
INSERT INTO theme (name, description, thumbnail)
VALUES ('사이버 해커', '가상 세계에서 미션 수행', 'https://ibb.co/example02');
INSERT INTO theme (name, description, thumbnail)
VALUES ('고대 유적', '유물을 찾아 떠나는 모험', 'https://ibb.co/example03');
INSERT INTO theme (name, description, thumbnail)
VALUES ('시간 여행자', '과거와 미래를 넘나드는 이야기', 'https://ibb.co/example04');

-- 회원
INSERT INTO member (name, email, password, role)
VALUES ('에드', 'ed@example.com', 'password1', 'admin');
INSERT INTO member (name, email, password, role)
VALUES ('슬링키', 'slinky@example.com', 'password2', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('라라', 'lara@example.com', 'password3', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('아서', 'arthur@example.com', 'password4', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('엠제이', 'mj@example.com', 'password5', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('율무', 'yulmu@example.com', 'password6', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('강산', 'gangsan@example.com', 'password7', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('띠용', 'ttyong@example.com', 'password8', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('저스틴', 'justin@example.com', 'password9', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('모다', 'moda@example.com', 'password10', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('벨로', 'bello@example.com', 'password11', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('머랭', 'meringue@example.com', 'password12', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('하루', 'haru@example.com', 'password13', 'user');
INSERT INTO member (name, email, password, role)
VALUES ('가콩', 'gakong@example.com', 'password14', 'user');

-- 예약
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (1, '2025-05-01', 2, 5); -- 우주 탈출
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2025-05-02', 3, 6); -- 사이버 해커
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (3, '2025-05-03', 4, 7); -- 고대 유적
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (4, '2025-05-04', 5, 8); -- 시간 여행자
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (5, '2025-05-09', 1, 1); -- 공포의 저택
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (6, '2025-05-09', 3, 2); -- 미래 도시
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (7, '2025-05-10', 6, 3); -- 탐정 사무소
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (8, '2025-05-11', 2, 4); -- 마법 학교
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (9, '2025-05-12', 4, 5); -- 우주 탈출
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (10, '2025-05-13', 7, 6); -- 사이버 해커
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (11, '2025-05-14', 5, 7); -- 고대 유적
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (12, '2025-05-15', 8, 8); -- 시간 여행자
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (13, '2025-05-15', 2, 1); -- 공포의 저택
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (14, '2025-05-16', 3, 2); -- 미래 도시
