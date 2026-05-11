-- 1. 예약 시간 (10:00 ~ 18:00, 1시간 간격)
INSERT INTO reservation_time (start_at) VALUES
                                            ('10:00'), ('11:00'), ('12:00'), ('13:00'), ('14:00'),
                                            ('15:00'), ('16:00'), ('17:00'), ('18:00');

-- 2. 테마 (총 15개)
INSERT INTO theme (name, description, image_url) VALUES
                                                    ('공포의 저택', '원혼이 떠도는 저택에서 탈출하세요.', 'https://i.namu.wiki/i/ojTADOEUumCcxaG06CC3aQFR8WFuBosqRQh1EqTM_nuts6spXisAEhT0asmls3ivaExtZWTEoFyyp9nWLdbYIw.webp'),
                                                    ('비밀 연구소', '바이러스가 퍼진 연구소.', 'https://imageoptimzer.acon3d.com/?image=https%3A%2F%2Fstorage.acon3d.com%2Fproduct%2F_-g6S3Wv3JQCPl7Dow1kQ&width=2600&quality=70&watermark=true'),
                                                    ('마법사의 방', '신비한 마법의 비밀을 푸세요.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMnr4SwV-NLMuZJsvkPzT4biFn3bSkZjFnPg&s'),
                                                    ('대탈옥', '최고 보안 교도소 탈출 작전.', 'https://an2-img.amz.wtchn.net/image/v2/z3C0-CpY-fYG4iczYQpgZA.jpg?jwt=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKdmNIUnpJanBiSW1SZk5EY3dlREkyTkhFNE1DSmRMQ0p3SWpvaUwzWXlMM04wYjNKbEwybHRZV2RsTHpFMk9URTNNelV6TkRZd09ERTBPRFk1TVRNaWZRLm9JWXFJQUlQQ1hjdm1iOExicmdEM1JiWWo1TWt1YVFBMEthZFNlWlp2ajg'),
                                                    ('해적선의 비밀', '저주받은 해적선 탐험.', 'https://image.yes24.com/momo/TopCate77/MidCate08/7671230.jpg'),
                                                    ('우주 미아', '고장난 우주선을 수리하세요.', 'https://i.ytimg.com/vi/cApabFGK8VA/maxresdefault.jpg'),
                                                    ('버려진 병원', '좀비 바이러스의 근원지.', 'https://t1.daumcdn.net/news/201810/22/yonhap/20181022080017373axet.jpg'),
                                                    ('고대 유적', '함정을 피해 보물을 찾으세요.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMcutuFXZHYJyslGlHR_7qYW9LbxSIXhhXjg&s'),
                                                    ('탐정 사무소', '미제 살인 사건의 전말.', 'https://i.namu.wiki/i/wPJYE6cNBd44v5qfcYrdYYQV_NQc1i97Tb5ptiqrNogdqrjkmyXn2875slzhTgvP36V78AIpxVg5863dQNzY1Q.webp'),
                                                    ('뱀파이어의 성', '드라큘라가 깨어나기 전에 탈출하세요.', 'https://png.pngtree.com/thumb_back/fh260/background/20231026/pngtree-spooky-vampire-s-den-gothic-castle-image_13944282.jpg'),
                                                    ('잃어버린 도시', '아틀란티스의 비밀.', 'https://mblogthumb-phinf.pstatic.net/MjAxODExMjVfNjkg/MDAxNTQzMTUxNDUwNDcy.7g-2_5R6dBWnnedm-JuJsDD8SEGoLYlhY41p-dZ2byIg.PL2MR9dUFZc-OzzaRF7ygk-fwi60XjqDGFJ9oCpa2kYg.JPEG.fb1874/1543151389101.jpg?type=w800'),
                                                    ('인형 괴담', '저주받은 인형의 방.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmY4znDOV_nz3Dlv9epLT27ZcPFNxM_ksBDQ&s'),
                                                    ('스파이 작전', '적진 한가운데 침투하세요.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnvHuNAUygb0aCP7qU_b4S7NKfebpMAy5DQg&s'),
                                                    ('지하 던전', '드래곤의 둥지를 무사히 지나가세요.', 'https://i.pinimg.com/236x/9d/84/0e/9d840ee387a240af4cc14310e72820f9.jpg'),
                                                    ('기억 조작소', '당신의 잃어버린 기억을 찾으세요.', 'https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/201307/26/htm_20130726365970107011.jpg');

-- 3. 예약 (총 105건, 1순위: 예약 건수 내림차순, 2순위: 테마 이름 오름차순)
INSERT INTO reservation (name, date, time_id, theme_id) VALUES
-- [32건] 테마 1: 공포의 저택
('김규빈', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('이철수', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),
('한지민', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 7, 1),
('김규빈', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('이효리', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),
('하하', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 6, 1),
('김종국', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),
('전소민', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 3, 1),
('유재석', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 7, 1),
('아이유', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('김지원', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
('이동욱', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
('김수현', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('신성록', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 5, 1),
('홍진경', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 9, 1),
('류준열', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('박보검', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 3, 1),
('최성원', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 7, 1),
('라미란', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('조정석', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
('신현빈', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 9, 1),
('곽선영', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('배현성', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
('최현욱', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
('정은지', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),
('이호원', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 3, 1),
('이일화', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 6, 1),
('고아라', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
('손호준', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1),
('김규빈', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 4, 1),
('안재홍', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 8, 1),
('류준열', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1),

-- [21건] 테마 2: 비밀 연구소
('박영희', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
('강호동', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
('이상순', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
('노홍철', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 7, 2),
('송지효', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 1, 2),
('이광수', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
('김종국', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
('송중기', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
('김고은', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),
('전지현', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
('남창희', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
('혜리', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
('성동일', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 8, 2),
('김선영', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 2, 2),
('정경호', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 7, 2),
('조이현', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 3, 2),
('이주명', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),
('이시언', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 4, 2),
('유연석', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),
('이철수', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 5, 2),
('이동휘', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 9, 2),

-- [12건] 테마 3: 마법사의 방
('최동석', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
('박명수', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
('양세찬', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
('박보검', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 2, 3),
('유인나', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 1, 3),
('박해진', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 3, 3),
('고경표', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 4, 3),
('김성균', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 2, 3),
('김대명', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 8, 3),
('김태리', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 6, 3),
('성동일', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 5, 3),
('박영희', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 6, 3),

-- [4건] 테마 4: 대탈옥
('김민수', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 5, 4),
('진구', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 5, 4),
('이무생', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 3, 4),
('바로', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 2, 4),

-- [4건] 테마 7: 버려진 병원
('신동엽', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 1, 7),
('육성재', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 2, 7),
('안은진', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 1, 7),
('고경표', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 1, 7),

-- [4건] 테마 6: 우주 미아
('유재석', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 9, 6),
('공유', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 7, 6),
('유연석', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 6, 6),
('최동석', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 7, 6),

-- [4건] 테마 5: 해적선의 비밀
('정수아', FORMATDATETIME(DATEADD(DAY, -10, CURRENT_DATE), 'yyyy-MM-dd'), 6, 5),
('김규빈', FORMATDATETIME(DATEADD(DAY, -7, CURRENT_DATE), 'yyyy-MM-dd'), 6, 5),
('전미도', FORMATDATETIME(DATEADD(DAY, -4, CURRENT_DATE), 'yyyy-MM-dd'), 5, 5),
('민도희', FORMATDATETIME(DATEADD(DAY, -1, CURRENT_DATE), 'yyyy-MM-dd'), 3, 5),

-- [3건] 테마 8: 고대 유적
('정준하', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 5, 8),
('유인나', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 4, 8),
('김준한', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 2, 8),

-- [3건] 테마 15: 기억 조작소
('이철수', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 1, 15),
('류혜영', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 1, 15),
('김성균', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 1, 15),

-- [3건] 테마 10: 뱀파이어의 성
('길', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 9, 10),
('조세호', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 7, 10),
('보나', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 7, 10),

-- [3건] 테마 13: 스파이 작전
('하하', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 6, 13),
('안재홍', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 6, 13),
('은지원', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 2, 13),

-- [3건] 테마 12: 인형 괴담
('지석진', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 5, 12),
('이동휘', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 5, 12),
('신소율', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 1, 12),

-- [3건] 테마 11: 잃어버린 도시
('데프콘', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 1, 11),
('이동휘', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 1, 11),
('서인국', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 1, 11),

-- [3건] 테마 14: 지하 던전
('박영희', FORMATDATETIME(DATEADD(DAY, -8, CURRENT_DATE), 'yyyy-MM-dd'), 9, 14),
('이일화', FORMATDATETIME(DATEADD(DAY, -5, CURRENT_DATE), 'yyyy-MM-dd'), 9, 14),
('정우', FORMATDATETIME(DATEADD(DAY, -2, CURRENT_DATE), 'yyyy-MM-dd'), 7, 14),

-- [3건] 테마 9: 탐정 사무소
('정형돈', FORMATDATETIME(DATEADD(DAY, -9, CURRENT_DATE), 'yyyy-MM-dd'), 8, 9),
('안재현', FORMATDATETIME(DATEADD(DAY, -6, CURRENT_DATE), 'yyyy-MM-dd'), 6, 9),
('남주혁', FORMATDATETIME(DATEADD(DAY, -3, CURRENT_DATE), 'yyyy-MM-dd'), 5, 9);
