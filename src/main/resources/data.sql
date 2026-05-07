INSERT INTO times(start_at)
values ('10:00');
INSERT INTO times(start_at)
values ('12:00');
INSERT INTO times(start_at)
values ('14:00');
INSERT INTO times(start_at)
values ('16:00');
INSERT INTO times(start_at)
values ('18:00');
INSERT INTO times(start_at)
values ('20:00');

INSERT INTO themes(name, thumbnail_url, description)
VALUES ('냥이 점집', 'https://i.postimg.cc/3JRp43dK/1553676990.jpg', '난이도 NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('어느 구두쇠의 전시회장', 'https://i.postimg.cc/4yrMrRfQ/image.jpg', '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('준단화:몸을 잘라낸 꽃',
        'https://www.seoul-escape.com/storage/episode/2024_11/06/qnAHwzCVuvRU7x62epGGSUciARX22w08CsrMSBb9.png',
        '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('팩토리', 'https://www.seoul-escape.com/storage/episode/2024_11/06/qnAHwzCVuvRU7x62epGGSUciARX22w08CsrMSBb9.png',
        '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('고문실', 'https://www.seoul-escape.com/storage/episode/2022_11/09/hFa2HaQPrHERgVtstgwsVfdMGT69AGxRMRXpolRe.png',
        '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('엘리베이터', 'https://www.seoul-escape.com/storage/episode/2022_11/09/ZEiSp4KjRt6L47SroX8ikS0OoeR99nftI4ndeS2r.png',
        '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('접견', 'https://www.seoul-escape.com/storage/episode/2026_04/11/mbAc3GHBTmF9mWtXh6JJ1DW2lyioqzt5ih68Pnie.png',
        '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('오시리스', 'https://www.seoul-escape.com/storage/episode/2026_02/20/m9gnxCTeS22AbuXCNRJ4SKLzLtzk8NMEwuJzebb4.png',
        '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('만찬', 'https://www.seoul-escape.com/storage/episode/2026_02/20/m9gnxCTeS22AbuXCNRJ4SKLzLtzk8NMEwuJzebb4.png',
        '난이도: NORMAL 3/5');
INSERT INTO themes(name, thumbnail_url, description)
VALUES ('오모테나시', 'https://www.seoul-escape.com/storage/episode/2024_11/06/qnAHwzCVuvRU7x62epGGSUciARX22w08CsrMSBb9.png',
        '난이도: NORMAL 3/5');

-- 테마 1 (많이 예약되도록)
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('A', CURRENT_DATE, 10, 2);
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('B', CURRENT_DATE, 10, 3);
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('C', CURRENT_DATE, 10, 1);
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('D', CURRENT_DATE, 10, 4);

-- 테마 2 (중간 정도)
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('E', CURRENT_DATE, 9, 3);
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('F', CURRENT_DATE, 9, 1);

-- 테마 3 (적게)
INSERT INTO reservations(name, date, theme_id, time_id)

VALUES ('G', CURRENT_DATE, 8, 2);

-- 테마 4 (없음 → 테스트용)
-- intentionally no reservations

-- 다양한 날짜 테스트용
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('H', '2026-10-01', 10, 5);
INSERT INTO reservations(name, date, theme_id, time_id)
VALUES ('I', '2026-06-02', 9, 6);
