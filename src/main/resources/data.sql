-- 시간 (08:00 ~ 20:00, 3시간 간격, 5개)
INSERT INTO reservation_time (start_at) VALUES ('08:00');
INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('16:00');
INSERT INTO reservation_time (start_at) VALUES ('18:00');
INSERT INTO reservation_time (start_at) VALUES ('20:00');
INSERT INTO reservation_time (start_at) VALUES ('22:00');

-- 테마 (15개)
INSERT INTO theme (name, description, thumbnail) VALUES ('공포의 방', '심장이 멎을 듯한 공포 체험', 'https://example.com/horror.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('추리의 방', '명탐정이 되어 사건을 해결하라', 'https://example.com/mystery.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('SF 우주선', '미래 우주에서 살아남기', 'https://example.com/sf.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('판타지 왕국', '마법과 모험의 세계', 'https://example.com/fantasy.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('좀비 아포칼립스', '좀비로부터 도시를 탈출하라', 'https://example.com/zombie.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('이집트 무덤', '파라오의 저주를 풀어라', 'https://example.com/egypt.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('비밀 연구소', '의문의 실험실에서 탈출하기', 'https://example.com/lab.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('해적선', '보물을 찾아 떠나는 항해', 'https://example.com/pirate.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('우주정거장', '고장 난 우주정거장 탈출', 'https://example.com/space.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('마법학교', '마법사들의 비밀을 풀어라', 'https://example.com/magic.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('시간여행', '과거와 미래를 넘나드는 모험', 'https://example.com/time.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('고대 신전', '잃어버린 유적의 비밀', 'https://example.com/temple.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('닌자 마을', '그림자 속에서 임무를 완수하라', 'https://example.com/ninja.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('외계 행성', '미지의 행성에서 살아남기', 'https://example.com/alien.png');
INSERT INTO theme (name, description, thumbnail) VALUES ('초자연 현상', '설명할 수 없는 현상을 조사하라', 'https://example.com/paranormal.png');

-- 예약 (30개)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('홍길동', '2026-05-01', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김철수', '2026-05-02', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이영희', '2026-05-03', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박민수', '2026-05-04', 4, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('정유진', '2026-05-05', 5, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('최지훈', '2026-05-01', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('강수진', '2026-05-02', 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('윤서연', '2026-05-03', 4, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('조현우', '2026-05-04', 5, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('한지민', '2026-05-01', 3, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('임재현', '2026-05-02', 4, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('송혜교', '2026-05-03', 5, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이병헌', '2026-05-04', 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('전지현', '2026-05-01', 4, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('현빈', '2026-05-02', 5, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('손예진', '2026-05-03', 1, 4);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박서준', '2026-05-01', 5, 5);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('아이유', '2026-05-02', 1, 5);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('공유', '2026-05-03', 2, 5);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('차은우', '2026-05-01', 1, 6);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('수지', '2026-05-02', 2, 6);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박보검', '2026-05-01', 2, 7);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김연아', '2026-05-02', 3, 7);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('손흥민', '2026-05-01', 3, 8);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('황희찬', '2026-05-02', 4, 8);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('김연경', '2026-05-01', 4, 9);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('류현진', '2026-05-01', 5, 10);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이정후', '2026-05-01', 1, 11);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('박찬호', '2026-05-01', 2, 12);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('이대호', '2026-05-01', 3, 13);
