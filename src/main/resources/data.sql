-- 예약 시간 (5개)
INSERT INTO reservation_time (start_at)
VALUES ('10:00');
INSERT INTO reservation_time (start_at)
VALUES ('11:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00');
INSERT INTO reservation_time (start_at)
VALUES ('16:00');
INSERT INTO reservation_time (start_at)
VALUES ('19:00');

-- 테마 (5개)
INSERT INTO theme (name, description, thumbnail)
VALUES ('공포의 방', '심장 약한 사람은 들어오지 마세요. 공포 등급 최상.', 'https://example.com/themes/horror.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('미스터리 추리', '셜록이 되어 사건을 해결해보세요.', 'https://example.com/themes/mystery.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('우주 탈출', '고장난 우주선에서 1시간 안에 탈출하라.', 'https://example.com/themes/space.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('조선시대 암행어사', '암행어사가 되어 부패한 사또를 잡아라.', 'https://example.com/themes/joseon.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('초보자 방', '방탈출이 처음이신 분들을 위한 입문 테마.', 'https://example.com/themes/beginner.jpg');

-- 예약
-- 같은 날 같은 테마에 여러 시간 예약 (예약 가능 시간 조회 테스트용)
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('동키', 1, '2026-05-10', 1);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('그해', 1, '2026-05-10', 3);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('아루', 1, '2026-05-10', 5);

-- 같은 날 다른 테마
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('매트', 2, '2026-05-10', 2);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('동키', 3, '2026-05-10', 4);

-- 테마 5 (초보자 방) 5/10 전 시간 마감 - 예약 가능 시간 0개 케이스
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('동키', 5, '2026-05-10', 1);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('그해', 5, '2026-05-10', 2);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('아루', 5, '2026-05-10', 3);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('매트', 5, '2026-05-10', 4);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('동키', 5, '2026-05-10', 5);

INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('그해', 1, '2026-05-11', 1);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('아루', 2, '2026-05-11', 2);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('매트', 3, '2026-05-11', 3);

INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('동키', 1, '2026-05-12', 2);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('그해', 1, '2026-05-12', 4);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('아루', 2, '2026-05-12', 1);
INSERT INTO reservation (user_name, theme_id, date, time_id)
VALUES ('매트', 4, '2026-05-12', 3);
