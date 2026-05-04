INSERT INTO reservation_time (start_at)
VALUES ('18:00'),
       ('19:00'),
       ('20:00'),
       ('21:00');

INSERT INTO theme (name, description, image_url)
VALUES ('셜록: 런던의 그림자', '안개 낀 런던, 의문의 살인 사건 현장에 남겨진 마지막 단서를 찾아 범인을 검거하세요.',
        'https://images.unsplash.com/photo-1585076641399-5c0f74268a2d?auto=format&fit=crop&q=80&w=800'),
       ('폐쇄병동의 비밀', '버려진 정신병원, 자정마다 들려오는 비명소리... 당신은 이곳에서 제정신으로 나갈 수 있을까요?',
        'https://images.unsplash.com/photo-1509248961158-e54f6934749c?auto=format&fit=crop&q=80&w=800'),
       ('파라오의 저주', '모래 폭풍 속에 발견된 고대 피라미드. 함정을 피하고 숨겨진 황금 마스크를 찾아 탈출해야 합니다.',
        'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&q=80&w=800'),
       ('이상한 나라의 초대장', '토끼굴로 떨어진 당신, 모든 것이 거꾸로 된 세상에서 원래 세계로 돌아가는 길을 찾으세요.',
        'https://images.unsplash.com/photo-1582139329536-e7284fece509?auto=format&fit=crop&q=80&w=800');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('브라이언', '2026-05-10', 1, 1), -- 18:00, 셜록
       ('제이슨', '2026-05-10', 2, 2),  -- 19:00, 폐쇄병동
       ('앨리스', '2026-05-11', 3, 3),  -- 20:00, 파라오
       ('데이브', '2026-05-11', 4, 1); -- 21:00, 셜록
