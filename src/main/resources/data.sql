-- 예약 시간 데이터
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

-- 테마 데이터
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('우주 정거장', '우주 정거장에서 탈출하세요.', 'https://picsum.photos/seed/theme1/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('공포의 지하실', '지하실에 갇힌 당신, 살아남을 수 있을까요?', 'https://picsum.photos/seed/theme2/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('박물관이 살아있다', '밤이 되면 살아나는 박물관에서의 모험.', 'https://picsum.photos/seed/theme3/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('비밀의 정원', '아름답지만 치명적인 비밀이 숨겨진 정원.', 'https://picsum.photos/seed/theme4/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('미래 도시', '2124년 테크노 시티에서의 추격전.', 'https://picsum.photos/seed/theme5/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('해적선의 저주', '저주받은 해적선에서 보물을 찾아 탈출하세요.', 'https://picsum.photos/seed/theme6/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('사라진 탐정', '실종된 탐정의 사무실에서 단서를 찾으세요.', 'https://picsum.photos/seed/theme7/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('화성 탐사선', '화성 탐사선에서의 긴급 탈출.', 'https://picsum.photos/seed/theme8/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('중세 성터', '고대 성의 비밀 통로를 찾아보세요.', 'https://picsum.photos/seed/theme9/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('마법 학교', '마법 학교의 마지막 시험을 통과하세요.', 'https://picsum.photos/seed/theme10/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('잠입 수사', '적진 한복판에 잠입하여 정보를 탈취하세요.', 'https://picsum.photos/seed/theme11/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('눈 내리는 마을', '눈 덮인 마을의 따뜻한 이야기.', 'https://picsum.photos/seed/theme12/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('심해 탐사', '심해 속 신비로운 생명체와의 조우.', 'https://picsum.photos/seed/theme13/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('사막의 신기루', '끝없는 사막에서 길을 찾아보세요.', 'https://picsum.photos/seed/theme14/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('미로 공원', '복잡한 미로 속에서 출구를 찾으세요.', 'https://picsum.photos/seed/theme15/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('오래된 도서관', '먼지 쌓인 책들 속의 비밀 문장.', 'https://picsum.photos/seed/theme16/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('정글 탐험', '야생 정글에서의 생존 게임.', 'https://picsum.photos/seed/theme17/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('서커스 유랑단', '화려한 서커스 뒤에 감춰진 진실.', 'https://picsum.photos/seed/theme18/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('사이버 펑크', '네온 사인이 가득한 도시의 어두운 이면.', 'https://picsum.photos/seed/theme19/400/300');
INSERT INTO theme (name, description, thumbnail_url)
VALUES ('타임 머신', '과거와 미래를 넘나드는 시간 여행.', 'https://picsum.photos/seed/theme20/400/300');

-- 인기 테마 집계용 예약 데이터 (최근 7일 이내)
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이순신', '2026-05-04', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김유신', '2026-05-03', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('강감찬', '2026-05-02', 4, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('세종대왕', '2026-05-01', 5, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이순신', '2026-05-04', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김유신', '2026-05-03', 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('강감찬', '2026-05-02', 4, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이순신', '2026-05-04', 2, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김유신', '2026-05-03', 3, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이순신', '2026-05-04', 2, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('김유신', '2026-05-03', 3, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 5);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이순신', '2026-05-04', 2, 5);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 6);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이순신', '2026-05-04', 2, 6);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 7);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('이순신', '2026-05-04', 2, 7);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 8);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 9);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2026-05-05', 1, 10);
