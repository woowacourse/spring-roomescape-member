INSERT INTO theme (name, description, thumbnail) VALUES ('공포의 숲', '으스스한 숲 속에서 단서를 찾아 탈출하세요.', 'https://images.unsplash.com/photo-1509248961158-e54f6934749c?auto=format&fit=crop&w=500&q=80');
INSERT INTO theme (name, description, thumbnail) VALUES ('비밀의 연구실', '미치광이 과학자의 실험실에서 벗어나야 합니다.', 'https://images.unsplash.com/photo-1532094349884-543bc11b234d?auto=format&fit=crop&w=500&q=80');
INSERT INTO theme (name, description, thumbnail) VALUES ('우주 정거장', '산소가 고갈되기 60분 전, 무사히 귀환하세요.', 'https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?auto=format&fit=crop&w=500&q=80');
INSERT INTO theme (name, description, thumbnail) VALUES ('해적선의 보물', '저주받은 해적선에서 보물을 찾고 탈출하라!', 'https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=500&q=80');
INSERT INTO theme (name, description, thumbnail) VALUES ('폐병원 병동', '자정의 병원, 누군가 당신을 지켜보고 있다.', 'https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?auto=format&fit=crop&w=500&q=80');

INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');
INSERT INTO reservation_time (start_at) VALUES ('16:00');
INSERT INTO reservation_time (start_at) VALUES ('19:00');
INSERT INTO reservation_time (start_at) VALUES ('22:00');

-- 인기 테마 데이터 셋업 (어제, 그제 등 최근 7일 내 데이터 삽입)
-- Theme 1 (공포의 숲) - 4번의 예약 (1등)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('브라운', FORMATDATETIME(DATEADD('DAY', -1, CURRENT_DATE()), 'yyyy-MM-dd'), 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('코니', FORMATDATETIME(DATEADD('DAY', -2, CURRENT_DATE()), 'yyyy-MM-dd'), 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('샐리', FORMATDATETIME(DATEADD('DAY', -3, CURRENT_DATE()), 'yyyy-MM-dd'), 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('레너드', FORMATDATETIME(DATEADD('DAY', -5, CURRENT_DATE()), 'yyyy-MM-dd'), 4, 1);

-- Theme 2 (비밀의 연구실) - 3번의 예약 (2등)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('제임스', FORMATDATETIME(DATEADD('DAY', -1, CURRENT_DATE()), 'yyyy-MM-dd'), 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('에드워드', FORMATDATETIME(DATEADD('DAY', -4, CURRENT_DATE()), 'yyyy-MM-dd'), 3, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('제시카', FORMATDATETIME(DATEADD('DAY', -6, CURRENT_DATE()), 'yyyy-MM-dd'), 5, 2);

-- Theme 3 (우주 정거장) - 2번의 예약 (3등)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('문', FORMATDATETIME(DATEADD('DAY', -2, CURRENT_DATE()), 'yyyy-MM-dd'), 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('보스', FORMATDATETIME(DATEADD('DAY', -3, CURRENT_DATE()), 'yyyy-MM-dd'), 2, 3);

-- Theme 4 (해적선의 보물) - 1번의 예약 (4등)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('초코', FORMATDATETIME(DATEADD('DAY', -1, CURRENT_DATE()), 'yyyy-MM-dd'), 4, 4);

-- 과거 데이터 (7일 초과) -> 인기 조회에 포함되지 않아야 함.
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('과거유저', FORMATDATETIME(DATEADD('DAY', -10, CURRENT_DATE()), 'yyyy-MM-dd'), 1, 5);
