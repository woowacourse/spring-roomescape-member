-- 1. 테마 데이터 (총 12개)
INSERT INTO theme (id, name, description, image_url, running_time)
VALUES
    (1, '공포의 병원', '버려진 정신병원에서 탈출해야 합니다.', 'https://picsum.photos/200/300', 60),
    (2, '박물관 침입', '전설의 다이아몬드를 훔쳐 나오세요.', 'https://picsum.photos/200/300', 60),
    (3, '셜록의 서재', '명탐정의 서재 속에 숨겨진 비밀을 찾으세요.', 'https://picsum.photos/200/300', 60),
    (4, '우주선 탈출', '산소가 떨어지기 전에 지구로 귀환해야 합니다.', 'https://picsum.photos/200/300', 60),
    (5, '심해의 비밀', '심해 3000m 아래 기지에서 벌어지는 미스터리.', 'https://picsum.photos/200/300', 60),
    (6, '서부의 무법자', '현상금 사냥꾼이 되어 무법자를 잡으세요.', 'https://picsum.photos/200/300', 60),
    (7, '마법사의 방', '금지된 마법 주문을 완성해야 합니다.', 'https://picsum.photos/200/300', 60),
    (8, '감옥 탈출', '억울한 누명을 벗고 탈옥에 성공하세요.', 'https://picsum.photos/200/300', 60),
    (9, '버려진 놀이공원', '밤마다 들리는 회전목마 소리의 진실은?', 'https://picsum.photos/200/300', 60),
    (10, '고대 이집트의 저주', '파라오의 무덤 속 트랩을 피하세요.', 'https://picsum.photos/200/300', 60),
    (11, '해적선 보물찾기', '사라진 해적왕의 보물 지도를 찾으세요.', 'https://picsum.photos/200/300', 60),
    (12, '숲속의 오두막', '안개 낀 숲속, 길을 잃은 당신 앞에 나타난 오두막.', 'https://picsum.photos/200/300', 60);

-- 2. 예약 시간 데이터
INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00'),
       (2, '11:00'),
       (3, '12:00'),
       (4, '13:00'),
       (5, '14:00'),
       (6, '15:00');

-- 3. 예약 데이터 (오늘 기준 최근 7일 이내로 동적 할당)
-- 1, 2, 3, 4.
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES
    -- 1위: 공포의 병원 (5건) - 최근 1~5일 전 데이터
    ('루드비코', DATEADD('DAY', -7, CURRENT_DATE), 1, 1),
    ('어셔', DATEADD('DAY', -6, CURRENT_DATE), 2, 1),
    ('브라운', DATEADD('DAY', -5, CURRENT_DATE), 3, 1),
    ('제이슨', DATEADD('DAY', -4, CURRENT_DATE), 4, 1),
    ('고릴라', DATEADD('DAY', -3, CURRENT_DATE), 1, 1),

    -- 2위: 박물관 침입 (4건) - 최근 4~7일 전 데이터
    ('코코', DATEADD('DAY', -7, CURRENT_DATE), 1, 2),
    ('공구', DATEADD('DAY', -6, CURRENT_DATE), 2, 2),
    ('구구', DATEADD('DAY', -5, CURRENT_DATE), 3, 2),
    ('주디', DATEADD('DAY', -4, CURRENT_DATE), 4, 2),

    -- 3위: 셜록의 서재 (3건)
    ('참가자1', DATEADD('DAY', -3, CURRENT_DATE), 1, 3),
    ('참가자2', DATEADD('DAY', -2, CURRENT_DATE), 2, 3),
    ('참가자3', DATEADD('DAY', -2, CURRENT_DATE), 3, 3),

    -- 4위: 서부의 무법자 (2건)
    ('참가자4', DATEADD('DAY', -7, CURRENT_DATE), 1, 4),
    ('참가자5', DATEADD('DAY', -6, CURRENT_DATE), 2, 4),

    -- 5위: 심해의 비밀 (2건)
    ('참가자6', DATEADD('DAY', -5, CURRENT_DATE), 1, 5),
    ('참가자7', DATEADD('DAY', -4, CURRENT_DATE), 2, 5),

    -- 6위: 우주선 탈출 (2건)
    ('참가자8', DATEADD('DAY', -3, CURRENT_DATE), 1, 6),
    ('참가자9', DATEADD('DAY', -2, CURRENT_DATE), 2, 6),

    -- 7위: 감옥 탈출 (1건)
    ('참가자10', DATEADD('DAY', -2, CURRENT_DATE), 1, 7),

    -- 8위: 마법사의 방 (1건)
    ('참가자11', DATEADD('DAY', -7, CURRENT_DATE), 2, 8),

    -- 예외 케이스: 집계 범위 밖 (너무 과거이거나 오늘/미래 데이터)
    -- 15일 전
    ('나그네', DATEADD('DAY', -15, CURRENT_DATE), 1, 11),
    -- 오늘
    ('방문객', CURRENT_DATE, 2, 11);
