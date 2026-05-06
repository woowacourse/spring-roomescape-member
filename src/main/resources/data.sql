-- reservation_time
INSERT INTO reservation_time (start_at)
VALUES ('10:00');
INSERT INTO reservation_time (start_at)
VALUES ('11:00');
INSERT INTO reservation_time (start_at)
VALUES ('12:00');
INSERT INTO reservation_time (start_at)
VALUES ('13:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00');
INSERT INTO reservation_time (start_at)
VALUES ('15:00');
INSERT INTO reservation_time (start_at)
VALUES ('16:00');
INSERT INTO reservation_time (start_at)
VALUES ('17:00');
INSERT INTO reservation_time (start_at)
VALUES ('18:00');
INSERT INTO reservation_time (start_at)
VALUES ('19:00');

-- theme
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('미스터리 저택', '빅토리아 시대 영국의 의문스러운 저택을 탐험하세요', 'https://example.com/themes/mansion.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('우주 정거장', '고장난 우주 정거장에서 살아남으세요', 'https://example.com/themes/space.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('좀비 아포칼립스', '좀비가 점령한 도시에서 탈출하세요', 'https://example.com/themes/zombie.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('고대 이집트', '파라오의 무덤에 숨겨진 비밀을 풀어내세요', 'https://example.com/themes/egypt.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('해적선의 보물', '카리브해 해적선에서 보물을 찾아 탈출하세요', 'https://example.com/themes/pirate.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('폐쇄 병동', '버려진 병동의 어두운 비밀을 파헤치세요', 'https://example.com/themes/asylum.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('시간 여행자의 실험실', '시간 여행 실험에 갇힌 당신을 구하세요', 'https://example.com/themes/timelab.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('마법사의 탑', '사라진 마법사의 탑에서 주문을 풀어내세요', 'https://example.com/themes/wizard.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('침몰하는 잠수함', '가라앉는 잠수함에서 탈출하세요', 'https://example.com/themes/submarine.jpg');
INSERT INTO theme (name, description, thumbnail_image_url)
VALUES ('은행 금고', '삼엄한 경비를 뚫고 금고에서 탈출하세요', 'https://example.com/themes/bank.jpg');

-- reservation
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김민수', '2026-05-10', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이지영', '2026-05-10', 5, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('박서준', '2026-05-11', 7, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('정유나', '2026-05-11', 4, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('최도현', '2026-05-12', 6, 5);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('한소희', '2026-05-12', 8, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('강민호', '2026-05-13', 2, 7);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('윤채영', '2026-05-14', 9, 8);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('임재현', '2026-05-14', 5, 6);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('송하은', '2026-05-15', 6, 10);
