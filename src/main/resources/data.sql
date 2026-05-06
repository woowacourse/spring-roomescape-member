INSERT INTO reservation_time (start_at) VALUES ('09:00');
INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('15:00');
INSERT INTO reservation_time (start_at) VALUES ('16:00');
INSERT INTO reservation_time (start_at) VALUES ('17:00');
INSERT INTO reservation_time (start_at) VALUES ('18:00');

INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('추리 탐정사무소', '단서를 모아 사건을 해결하라', 'https://img.example.com/detective.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('SF 우주 탐험', '우주 정거장에서 탈출하라', 'https://img.example.com/space.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('마법사의 탑', '고대 마법의 비밀을 파헤쳐라', 'https://img.example.com/magic.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('해적의 보물', '전설의 보물을 차지하라', 'https://img.example.com/pirate.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('좀비 생존', '살아있는 시체들을 피해 탈출하라', 'https://img.example.com/zombie.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('비밀 연구소', '실험이 잘못됐다. 지금 당장 나가라', 'https://img.example.com/lab.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('타임머신 여행', '과거로 돌아가 역사를 바꿔라', 'https://img.example.com/time.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('용의 둥지', '잠든 용을 깨우지 마라', 'https://img.example.com/dragon.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('미궁의 성', '끝이 없는 미로에서 출구를 찾아라', 'https://img.example.com/maze.jpg');
INSERT INTO theme (name, description, thumbnail_img_url) VALUES ('고대 사원', '봉인된 유물을 찾아 사원을 빠져나가라', 'https://img.example.com/temple.jpg');

-- theme2: 10건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('카야', '2026-04-30', 2, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피노', '2026-04-30', 2, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('스타크', '2026-05-01', 2, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('로지', '2026-05-01', 2, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이산', '2026-05-02', 2, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('바니', '2026-05-02', 2, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('러키', '2026-05-03', 2, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('초록', '2026-05-03', 2, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이안', '2026-05-05', 2, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('소낙눈', '2026-05-05', 2, 2);

-- theme3: 9건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피즈', '2026-04-30', 3, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('도우너', '2026-04-30', 3, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('카야', '2026-05-01', 3, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('초록', '2026-05-01', 3, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('스타크', '2026-05-02', 3, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이안', '2026-05-02', 3, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('바니', '2026-05-03', 3, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('로지', '2026-05-03', 3, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피노', '2026-05-05', 3, 1);

-- theme4: 8건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('러키', '2026-04-30', 4, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이산', '2026-04-30', 4, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('소낙눈', '2026-05-01', 4, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('도우너', '2026-05-01', 4, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피즈', '2026-05-02', 4, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('초록', '2026-05-02', 4, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('카야', '2026-05-03', 4, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('스타크', '2026-05-03', 4, 2);

-- theme5: 7건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('로지', '2026-04-30', 5, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('바니', '2026-04-30', 5, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이안', '2026-05-01', 5, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피노', '2026-05-01', 5, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('러키', '2026-05-02', 5, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('소낙눈', '2026-05-02', 5, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('도우너', '2026-05-03', 5, 1);

-- theme6: 6건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('초록', '2026-04-30', 6, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('스타크', '2026-04-30', 6, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피즈', '2026-05-01', 6, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('카야', '2026-05-01', 6, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이산', '2026-05-02', 6, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('로지', '2026-05-02', 6, 2);

-- theme7: 5건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('바니', '2026-04-30', 7, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이안', '2026-04-30', 7, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('러키', '2026-05-01', 7, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피노', '2026-05-01', 7, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('소낙눈', '2026-05-02', 7, 1);

-- theme8: 4건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('도우너', '2026-04-30', 8, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('초록', '2026-04-30', 8, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피즈', '2026-05-01', 8, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('스타크', '2026-05-01', 8, 2);

-- theme9: 3건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('카야', '2026-04-30', 9, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('로지', '2026-04-30', 9, 2);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이산', '2026-05-01', 9, 1);

-- theme10: 2건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이안', '2026-04-30', 10, 1);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('바니', '2026-04-30', 10, 2);

-- theme1: 1건
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('러키', '2026-04-30', 1, 3);

INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('소낙눈', '2026-04-25', 2, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피노', '2026-04-26', 2, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('도우너', '2026-04-27', 2, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('스타크', '2026-04-28', 2, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('초록', '2026-04-28', 2, 3);

INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('피즈', '2026-04-25', 10, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('카야', '2026-04-26', 10, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('이산', '2026-04-27', 10, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('로지', '2026-04-28', 10, 3);
INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('러키', '2026-04-29', 10, 3);
