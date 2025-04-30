-- reservation_time
INSERT INTO reservation_time (start_at)
VALUES ('10:00');
INSERT INTO reservation_time (start_at)
VALUES ('11:00');
INSERT INTO reservation_time (start_at)
VALUES ('12:00');

-- theme
INSERT INTO theme (name, description, thumbnail)
VALUES ('레벨2 탈출',
        '우테코 레벨2를 탈출하는 내용입니다.',
        'https://example.com/image.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('지하 감옥',
        '깊은 감옥에서 탈출하라!',
        'https://example.com/jail.jpg');

-- reservation: 오래된 데이터 (10~19일 전)
-- 날짜는 예시로 2025-04-11 ~ 2025-04-20 (today = 2025-04-30 가정)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자10', '2025-04-20', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자11', '2025-04-19', 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자12', '2025-04-18', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자13', '2025-04-17', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자14', '2025-04-16', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자15', '2025-04-15', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자16', '2025-04-14', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자17', '2025-04-13', 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자18', '2025-04-12', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('오래된예약자19', '2025-04-11', 2, 2);

-- reservation: 최근 데이터 (1~7일 전)
-- 날짜는 예시로 2025-04-23 ~ 2025-04-29 (today = 2025-04-30 가정)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최근예약자1', '2025-04-29', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최근예약자2', '2025-04-28', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최근예약자3', '2025-04-27', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최근예약자4', '2025-04-26', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최근예약자5', '2025-04-25', 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최근예약자6', '2025-04-24', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최근예약자7', '2025-04-23', 2, 2);