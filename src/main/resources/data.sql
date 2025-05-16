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

-- member 테이블 초기 데이터
INSERT INTO member (name, email, password, role) VALUES
                                              ('웨이드', 'wade@nnnn.com', 'password', 'normal'),
                                              ('모코', 'moko@nnnn.com', 'password', 'admin'),
                                              ('리사', 'lisa@nnnn.com', 'password', 'normal'),
                                              ('검프', 'gump@nnnn.com', 'password', 'normal');

-- reservation 테이블 초기 데이터
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES
                                                            (1, '2025-05-01', 1, 2),
                                                            (2, '2025-05-01', 2, 1),
                                                            (3, '2025-05-02', 3, 3),
                                                            (4, '2025-05-02', 1, 3);
