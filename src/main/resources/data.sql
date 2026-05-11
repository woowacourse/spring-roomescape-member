-- Reservation times: 10:00 ~ 22:00 (1-hour intervals, IDs 1~13)
INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('15:00');
INSERT INTO reservation_time (start_at) VALUES ('16:00');
INSERT INTO reservation_time (start_at) VALUES ('17:00');
INSERT INTO reservation_time (start_at) VALUES ('18:00');
INSERT INTO reservation_time (start_at) VALUES ('19:00');
INSERT INTO reservation_time (start_at) VALUES ('20:00');
INSERT INTO reservation_time (start_at) VALUES ('21:00');
INSERT INTO reservation_time (start_at) VALUES ('22:00');

-- Themes (IDs 1~4)
INSERT INTO theme (name, description, thumbnail_url) VALUES ('공포의 저택', '버려진 저택에서 탈출하라! 어둠 속에 숨겨진 비밀을 밝혀야 살 수 있다.', 'https://picsum.photos/seed/haunted/400/250');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('우주 정거장', '고장난 우주 정거장에서 살아남아라. 산소가 30분 후면 바닥난다!', 'https://picsum.photos/seed/spacestation/400/250');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('마법사의 연구실', '미친 마법사의 연구실에서 탈출하라. 다음 실험 대상이 되기 전에!', 'https://picsum.photos/seed/wizard/400/250');
INSERT INTO theme (name, description, thumbnail_url) VALUES ('탐정 사무소', '살인 사건의 유일한 용의자가 되었다. 진범을 찾아 무죄를 증명하라.', 'https://picsum.photos/seed/detective/400/250');

-- Reservations for popular theme ranking (range: today-7 ~ today-1)
-- 기준: 오늘이 2026-05-11이면 집계 범위는 2026-05-04 ~ 2026-05-10

-- 공포의 저택 (theme 1) - 5 bookings → 1st popular
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김철수', CURRENT_DATE - 7, 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이영희', CURRENT_DATE - 6, 5, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박민준', CURRENT_DATE - 5, 7, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('최수진', CURRENT_DATE - 4, 4, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('정다은', CURRENT_DATE - 3, 8, 1);

-- 탐정 사무소 (theme 4) - 4 bookings → 2nd popular
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('강현수', CURRENT_DATE - 6, 6, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('윤지원', CURRENT_DATE - 5, 9, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('임서준', CURRENT_DATE - 4, 11, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('한지아', CURRENT_DATE - 3, 3, 4);

-- 마법사의 연구실 (theme 3) - 3 bookings → 3rd popular
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김철수', CURRENT_DATE - 5, 2, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이영희', CURRENT_DATE - 2, 6, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박민준', CURRENT_DATE - 1, 10, 3);

-- 우주 정거장 (theme 2) - 2 bookings → 4th popular
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('최수진', CURRENT_DATE - 3, 4, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('강현수', CURRENT_DATE - 1, 8, 2);

-- Future reservations
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김철수', CURRENT_DATE + 7, 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이영희', CURRENT_DATE + 1, 5, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김철수', CURRENT_DATE + 4, 7, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박민준', CURRENT_DATE + 5, 9, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('최수진', CURRENT_DATE + 6, 1, 1);