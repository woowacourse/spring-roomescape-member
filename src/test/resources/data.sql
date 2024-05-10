-- INSERT INTO theme(name, description, thumbnail) VALUES ('테바와 비밀친구', '나랑.. 비밀친구할래..?', '테바 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('켈리의 댄스교실', '켈켈켈켈켈', '켈리 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('우테코 탈출하기', '우테코를... 탈출..하자..!', '우테코 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('네오의 두근두근 피드백 강의', '??? : 네오가 setter 쓰라고 했는데요?', '분노하는 네오 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('리사의 소프트 교육', '에궁..ㅜㅜ', '공감하는 리사 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('신천직화 탈출하기', '저희 3인분 시켰는데 왜 계란말이 안주세요?', '제육 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('장미상가 탈출하기', '여기 A동 아니에요?', '장미상가 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('페드로의 주먹', '페급~ (페드로 급이라는 뜻~)', '페드로 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('사물함 탈취하기', '니 사물함 쩔더라 ㅋ', '사물함 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('아루의 입기타', '뚜루루루룰루루루룰', '아루 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('이든의 프로틴 쉐이크 제작 강의', '내 단백질 어디있죠?', '이든 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('솔라의 솔라빔', '자라나라 머리머리!', '솔라 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('포비의 긴급 포수타', '포비는 크루들에게 실망했다.', '포비 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('브리와 솔라의 페어프로그래밍 연극', '... 이거 이렇게 짜는거 맞아요?', '훈훈한 브리와 솔라 사진');
-- INSERT INTO theme(name, description, thumbnail) VALUES ('레디의 코드리뷰', '아씨 깜짝아! 내 코드인줄 알았네 (제제의 코드를 보며)', '레디 사진');
--
-- INSERT INTO reservation_time(start_at) VALUES ('09:30');
-- INSERT INTO reservation_time(start_at) VALUES ('11:30');
-- INSERT INTO reservation_time(start_at) VALUES ('13:30');
-- INSERT INTO reservation_time(start_at) VALUES ('15:30');
-- INSERT INTO reservation_time(start_at) VALUES ('17:30');
-- INSERT INTO reservation_time(start_at) VALUES ('19:30');
-- INSERT INTO reservation_time(start_at) VALUES ('21:30');
-- INSERT INTO reservation_time(start_at) VALUES ('23:30');
--
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('켈리', CAST(TIMESTAMPADD(DAY, -3, NOW()) AS DATE), 1, 1);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('오리', CAST(TIMESTAMPADD(DAY, -4, NOW()) AS DATE), 1, 2);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('치킨보이', CAST(TIMESTAMPADD(DAY, -2, NOW()) AS DATE), 4, 1);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('치킨걸', CAST(TIMESTAMPADD(DAY, -6, NOW()) AS DATE), 3, 1);
--
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('테바', CAST(TIMESTAMPADD(DAY, -3, NOW()) AS DATE), 5, 1);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('파랑', CAST(TIMESTAMPADD(DAY, -4, NOW()) AS DATE), 7, 2);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('네오', CAST(TIMESTAMPADD(DAY, -2, NOW()) AS DATE), 6, 13);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('워니', CAST(TIMESTAMPADD(DAY, -6, NOW()) AS DATE), 8, 14);
--
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('커비', CAST(TIMESTAMPADD(DAY, -3, NOW()) AS DATE), 1, 2);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('안나', CAST(TIMESTAMPADD(DAY, -4, NOW()) AS DATE), 1, 12);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('러쉬', CAST(TIMESTAMPADD(DAY, -2, NOW()) AS DATE), 4, 10);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('레모네', CAST(TIMESTAMPADD(DAY, -6, NOW()) AS DATE), 3, 11);
--
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('호기', CAST(TIMESTAMPADD(DAY, 3, NOW()) AS DATE), 1, 10);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('아루', CAST(TIMESTAMPADD(DAY, 4, NOW()) AS DATE), 1, 15);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('재즈', CAST(TIMESTAMPADD(DAY, 2, NOW()) AS DATE), 4, 9);
-- INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('짱수', CAST(TIMESTAMPADD(DAY, 6, NOW()) AS DATE), 3, 8);

INSERT INTO theme(name, description, thumbnail)
VALUES ('테바와 비밀친구', '나랑.. 비밀친구할래..?', '테바 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('켈리의 댄스교실', '켈켈켈켈켈', '켈리 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('우테코 탈출하기', '우테코를... 탈출..하자..!', '우테코 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('네오의 두근두근 피드백 강의', '??? : 네오가 setter 쓰라고 했는데요?', '분노하는 네오 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('리사의 소프트 교육', '에궁..ㅜㅜ', '공감하는 리사 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('신천직화 탈출하기', '저희 3인분 시켰는데 왜 계란말이 안주세요?', '제육 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('장미상가 탈출하기', '여기 A동 아니에요?', '장미상가 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('페드로의 주먹', '페급~ (페드로 급이라는 뜻~)', '페드로 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('사물함 탈취하기', '니 사물함 쩔더라 ㅋ', '사물함 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('아루의 입기타', '뚜루루루룰루루루룰', '아루 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('이든의 프로틴 쉐이크 제작 강의', '내 단백질 어디있죠?', '이든 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('솔라의 솔라빔', '자라나라 머리머리!', '솔라 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('포비의 긴급 포수타', '포비는 크루들에게 실망했다.', '포비 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('브리와 솔라의 페어프로그래밍 연극', '... 이거 이렇게 짜는거 맞아요?', '훈훈한 브리와 솔라 사진');
INSERT INTO theme(name, description, thumbnail)
VALUES ('레디의 코드리뷰', '아씨 깜짝아! 내 코드인줄 알았네 (제제의 코드를 보며)', '레디 사진');

INSERT INTO reservation_time(start_at)
VALUES ('09:30');
INSERT INTO reservation_time(start_at)
VALUES ('11:30');
INSERT INTO reservation_time(start_at)
VALUES ('13:30');
INSERT INTO reservation_time(start_at)
VALUES ('15:30');
INSERT INTO reservation_time(start_at)
VALUES ('17:30');
INSERT INTO reservation_time(start_at)
VALUES ('19:30');
INSERT INTO reservation_time(start_at)
VALUES ('21:30');
INSERT INTO reservation_time(start_at)
VALUES ('23:30');

INSERT INTO role(name)
VALUES ('USER');
INSERT INTO role (name)
VALUES ('ADMIN');

INSERT INTO member(name, email, password, role_id)
VALUES ('켈리', 'kelly@example.com', 'password123', 1),
       ('오리', 'duck@example.com', 'password456', 1),
       ('치킨보이', 'chickenboy@example.com', 'password789', 1),
       ('치킨걸', 'chickengirl@example.com', 'password123', 1),
       ('테바', 'teba@example.com', 'password456', 1),
       ('파랑', 'blue@example.com', 'password789', 1),
       ('네오', 'neo@example.com', 'password123', 2),
       ('워니', 'wony@example.com', 'password456', 2),
       ('커비', 'kirby@example.com', 'password789', 2),
       ('안나', 'anna@example.com', 'password123', 2),
       ('러쉬', 'rush@example.com', 'password456', 2),
       ('레모네', 'lemon@example.com', 'password789', 2),
       ('호기', 'hogi@example.com', 'password123', 1),
       ('아루', 'aru@example.com', 'password456', 1),
       ('재즈', 'jazz@example.com', 'password789', 1),
       ('짱수', 'jangsoo@example.com', 'password123', 1);

INSERT INTO reservation(member_id, date, time_id, theme_id)
VALUES (1, CAST(TIMESTAMPADD(DAY, -3, NOW()) AS DATE), 1, 1),
       (2, CAST(TIMESTAMPADD(DAY, -4, NOW()) AS DATE), 1, 2),
       (3, CAST(TIMESTAMPADD(DAY, -2, NOW()) AS DATE), 4, 1),
       (4, CAST(TIMESTAMPADD(DAY, -6, NOW()) AS DATE), 3, 1),
       (5, CAST(TIMESTAMPADD(DAY, -3, NOW()) AS DATE), 5, 1),
       (6, CAST(TIMESTAMPADD(DAY, -4, NOW()) AS DATE), 7, 2),
       (7, CAST(TIMESTAMPADD(DAY, -2, NOW()) AS DATE), 6, 13),
       (8, CAST(TIMESTAMPADD(DAY, -6, NOW()) AS DATE), 8, 14),
       (9, CAST(TIMESTAMPADD(DAY, -3, NOW()) AS DATE), 1, 2),
       (10, CAST(TIMESTAMPADD(DAY, -4, NOW()) AS DATE), 1, 12),
       (11, CAST(TIMESTAMPADD(DAY, -2, NOW()) AS DATE), 4, 10),
       (12, CAST(TIMESTAMPADD(DAY, -6, NOW()) AS DATE), 3, 11),
       (13, CAST(TIMESTAMPADD(DAY, 3, NOW()) AS DATE), 1, 10),
       (14, CAST(TIMESTAMPADD(DAY, 4, NOW()) AS DATE), 1, 15),
       (15, CAST(TIMESTAMPADD(DAY, 2, NOW()) AS DATE), 4, 9),
       (16, CAST(TIMESTAMPADD(DAY, 6, NOW()) AS DATE), 3, 8);
