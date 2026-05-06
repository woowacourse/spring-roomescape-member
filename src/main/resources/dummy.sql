-- 1. 예약 시간 (10:00 ~ 18:00, 1시간 간격)
INSERT INTO reservation_time (start_at) VALUES
                                            ('10:00'), ('11:00'), ('12:00'), ('13:00'), ('14:00'),
                                            ('15:00'), ('16:00'), ('17:00'), ('18:00');

-- 2. 테마 (총 15개)
INSERT INTO theme (name, description, image_url) VALUES
                                                    ('공포의 저택', '원혼이 떠도는 저택에서 탈출하세요.', 'url1'),
                                                    ('비밀 연구소', '바이러스가 퍼진 연구소.', 'url2'),
                                                    ('마법사의 방', '신비한 마법의 비밀을 푸세요.', 'url3'),
                                                    ('대탈옥', '최고 보안 교도소 탈출 작전.', 'url4'),
                                                    ('해적선의 비밀', '저주받은 해적선 탐험.', 'url5'),
                                                    ('우주 미아', '고장난 우주선을 수리하세요.', 'url6'),
                                                    ('버려진 병원', '좀비 바이러스의 근원지.', 'url7'),
                                                    ('고대 유적', '함정을 피해 보물을 찾으세요.', 'url8'),
                                                    ('탐정 사무소', '미제 살인 사건의 전말.', 'url9'),
                                                    ('뱀파이어의 성', '드라큘라가 깨어나기 전에 탈출하세요.', 'url10'),
                                                    ('잃어버린 도시', '아틀란티스의 비밀.', 'url11'),
                                                    ('인형 괴담', '저주받은 인형의 방.', 'url12'),
                                                    ('스파이 작전', '적진 한가운데 침투하세요.', 'url13'),
                                                    ('지하 던전', '드래곤의 둥지를 무사히 지나가세요.', 'url14'),
                                                    ('기억 조작소', '당신의 잃어버린 기억을 찾으세요.', 'url15');

-- 3. 예약 (총 105건, 인기 테마 검증용 데이터 편향 적용)
-- H2 Database 전용 문법 적용 (DATEADD, FORMATDATETIME)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES
-- 10일 전 (10건)
    ('김규빈', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('이철수', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),
    ('박영희', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
    ('최동석', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
    ('김민수', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 5, 4),
    ('정수아', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 6, 5),
    ('한지민', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 7, 1),
    ('강호동', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
    ('유재석', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 9, 6),
    ('신동엽', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 1, 7),

-- 9일 전 (11건)
    ('김규빈', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('이효리', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),
    ('이상순', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
    ('박명수', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
    ('정준하', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 5, 8),
    ('하하', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 6, 1),
    ('노홍철', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 7, 2),
    ('정형돈', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 8, 9),
    ('길', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 9, 10),
    ('데프콘', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 1, 11),
    ('김종국', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),

-- 8일 전 (10건)
    ('송지효', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 1, 2),
    ('이광수', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
    ('전소민', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 3, 1),
    ('양세찬', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
    ('지석진', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 5, 12),
    ('하하', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 6, 13),
    ('유재석', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 7, 1),
    ('김종국', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
    ('박영희', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 9, 14),
    ('이철수', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 1, 15),

-- 7일 전 (11건)
    ('아이유', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('박보검', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 2, 3),
    ('송중기', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
    ('김지원', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
    ('진구', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 5, 4),
    ('김규빈', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 6, 5),
    ('공유', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 7, 6),
    ('이동욱', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
    ('김고은', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),
    ('유인나', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 1, 3),
    ('육성재', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 2, 7),

-- 6일 전 (10건)
    ('김수현', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('전지현', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
    ('박해진', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 3, 3),
    ('유인나', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 4, 8),
    ('신성록', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 5, 1),
    ('안재현', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 6, 9),
    ('조세호', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 7, 10),
    ('남창희', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
    ('홍진경', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 9, 1),
    ('이동휘', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 1, 11),

-- 5일 전 (11건)
    ('류준열', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('혜리', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
    ('박보검', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 3, 1),
    ('고경표', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
    ('이동휘', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 5, 12),
    ('안재홍', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 6, 13),
    ('최성원', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 7, 1),
    ('성동일', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
    ('이일화', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 9, 14),
    ('류혜영', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 1, 15),
    ('김성균', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 2, 3),

-- 4일 전 (10건)
    ('라미란', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('김선영', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
    ('이무생', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 3, 4),
    ('조정석', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
    ('전미도', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 5, 5),
    ('유연석', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 6, 6),
    ('정경호', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 7, 2),
    ('김대명', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 8, 3),
    ('신현빈', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 9, 1),
    ('안은진', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 1, 7),

-- 3일 전 (11건)
    ('곽선영', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('김준한', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 2, 8),
    ('조이현', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
    ('배현성', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
    ('남주혁', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 5, 9),
    ('김태리', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 6, 3),
    ('보나', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 7, 10),
    ('최현욱', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
    ('이주명', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),
    ('서인국', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 1, 11),
    ('정은지', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),

-- 2일 전 (10건)
    ('신소율', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 1, 12),
    ('은지원', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 2, 13),
    ('이호원', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 3, 1),
    ('이시언', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 4, 2),
    ('성동일', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 5, 3),
    ('이일화', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 6, 1),
    ('정우', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 7, 14),
    ('고아라', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
    ('유연석', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),
    ('김성균', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 1, 15),

-- 1일 전 (11건)
    ('손호준', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
    ('바로', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 2, 4),
    ('민도희', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 3, 5),
    ('김규빈', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
    ('이철수', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 5, 2),
    ('박영희', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 6, 3),
    ('최동석', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 7, 6),
    ('안재홍', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
    ('이동휘', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),
    ('고경표', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 1, 7),
    ('류준열', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1);
