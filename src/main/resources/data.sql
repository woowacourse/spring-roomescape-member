INSERT INTO theme (name, description, thumbnail)
VALUES ('공포의 저택', '폐가에서 벌어지는 미스터리 추리', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('타임머신 연구소', '시간을 거슬러 과거를 바꿔라', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('사라진 화가', '화가의 흔적을 따라가는 예술 추리물', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('디스토피아 감옥', '미래형 감옥에서의 탈출극', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('마법학교 입학시험', '마법사가 되기 위한 퍼즐 통과 미션', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('좀비 연구소', '감염된 연구소에서 살아남기', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('추억의 놀이공원', '폐쇄된 놀이공원의 숨겨진 이야기', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('사막의 유적', '고대 유물 속에 숨겨진 비밀', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('해저 도시의 비밀', '바다 밑 도시에서의 미션 수행', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('AI의 반란', '인공지능 통제실을 점령하라', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation_time (start_at)
VALUES ('10:00:00');
INSERT INTO reservation_time (start_at)
VALUES ('11:00:00');
INSERT INTO reservation_time (start_at)
VALUES ('12:00:00');
INSERT INTO reservation_time (start_at)
VALUES ('13:00:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00:00');
INSERT INTO reservation_time (start_at)
VALUES ('15:00:00');
INSERT INTO reservation_time (start_at)
VALUES ('16:00:00');
INSERT INTO reservation_time (start_at)
VALUES ('17:00:00');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2025-04-25', 1, 1),
       ('김철수', '2025-04-26', 2, 2),
       ('이영희', '2025-04-27', 3, 3),
       ('박지민', '2025-04-28', 4, 4),
       ('최유리', '2025-04-29', 5, 5),
       ('장원영', '2025-04-30', 6, 6),
       ('한지민', '2025-05-01', 7, 7),
       ('정해인', '2025-05-02', 8, 8),
       ('김태리', '2025-05-03', 1, 9),
       ('손흥민', '2025-05-04', 2, 10);


