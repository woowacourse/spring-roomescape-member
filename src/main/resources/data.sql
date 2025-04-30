-- reservation_time 테이블 초기 데이터
INSERT INTO reservation_time (start_at) VALUES
                                            ('10:00'),
                                            ('12:00'),
                                            ('14:00'),
                                            ('16:00');

-- theme 테이블 초기 데이터
INSERT INTO theme (name, description, thumbnail) VALUES
                                                     ('공포의 우테코', '우테코에서 벌어지는 미스터리를 풀어라', '/image/horror.png'),
                                                     ('시간 도둑', '스릴 넘치는 우테코 미션', '/image/time.png'),
                                                     ('우테코 학교', '잃어버린 DDD를 찾아라', '/image/school.png');

-- reservation 테이블 초기 데이터
INSERT INTO reservation (name, date, time_id, theme_id) VALUES
                                                            ('웨이드', '2025-05-01', 1, 2),
                                                            ('모코', '2025-05-01', 2, 1),
                                                            ('리사', '2025-05-02', 3, 3),
                                                            ('검프', '2025-05-02', 1, 3);
