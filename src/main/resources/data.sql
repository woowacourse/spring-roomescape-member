-- 샘플 데이터 삽입
INSERT INTO reservation_time (start_at)
VALUES ('10:00:00'),
       ('12:00:00'),
       ('14:00:00'),
       ('16:00:00'),
       ('18:00:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('방탈출 1', '초보자용 방탈출입니다.', 'thumbnail1.jpg'),
       ('방탈출 2', '중급자용 방탈출입니다.', 'thumbnail2.jpg'),
       ('방탈출 3', '고급자용 방탈출입니다.', 'thumbnail3.jpg');

-- 예시 예약 데이터
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김철수', CURRENT_DATE, 1, 1),
       ('이영희', CURRENT_DATE, 2, 2);
