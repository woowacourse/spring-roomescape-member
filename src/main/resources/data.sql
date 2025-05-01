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

-- 예약 (날짜: 2025-04-20 ~ 2025-04-30 사이 14개)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김철수', '2025-04-20', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이영희', '2025-04-20', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('박민수', '2025-04-21', 3, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최지현', '2025-04-22', 4, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('정우성', '2025-04-23', 5, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('한지민', '2025-04-24', 6, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('장동건', '2025-04-24', 7, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('송혜교', '2025-04-25', 8, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('배수지', '2025-04-26', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('남주혁', '2025-04-27', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김태리', '2025-04-28', 3, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('서강준', '2025-04-29', 4, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김고은', '2025-04-30', 5, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이도현', '2025-04-30', 6, 2);
