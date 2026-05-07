INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (1, '우주 정거장', '우주 정거장에서 탈출하세요.', 'https://picsum.photos/seed/theme1/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (2, '공포의 지하실', '지하실에 갇힌 당신, 살아남을 수 있을까요?', 'https://picsum.photos/seed/theme2/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (3, '박물관이 살아있다', '밤이 되면 살아나는 박물관에서의 모험.', 'https://picsum.photos/seed/theme3/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (4, '비밀의 정원', '아름답지만 치명적인 비밀이 숨겨진 정원.', 'https://picsum.photos/seed/theme4/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (5, '미래 도시', '2124년 테크노 시티에서의 추격전.', 'https://picsum.photos/seed/theme5/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (6, '해적선의 저주', '저주받은 해적선에서 보물을 찾아 탈출하세요.', 'https://picsum.photos/seed/theme6/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (7, '사라진 탐정', '실종된 탐정의 사무실에서 단서를 찾으세요.', 'https://picsum.photos/seed/theme7/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (8, '화성 탐사선', '화성 탐사선에서의 긴급 탈출.', 'https://picsum.photos/seed/theme8/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (9, '중세 성터', '고대 성의 비밀 통로를 찾아보세요.', 'https://picsum.photos/seed/theme9/400/300');
INSERT INTO theme (id, name, description, thumbnail_url)
VALUES (10, '마법 학교', '마법 학교의 마지막 시험을 통과하세요.', 'https://picsum.photos/seed/theme10/400/300');
ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 11;
