INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('15:00');
INSERT INTO reservation_time (start_at) VALUES ('16:00');
INSERT INTO reservation_time (start_at) VALUES ('17:00');

INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('비밀 연구소', '폐쇄된 연구소의 보안 시스템을 해제하고 제한 시간 안에 탈출하세요.', 'https://images.unsplash.com/photo-1532187863486-abf9dbad1b69?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('탐정 사무소', '사라진 의뢰인의 행방을 단서와 암호로 추적하는 추리 테마입니다.', 'https://images.unsplash.com/photo-1505664194779-8beaceb93744?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('우주 정거장', '산소 공급이 끊긴 정거장에서 귀환 좌표를 복구해야 합니다.', 'https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('고성의 초대장', '낡은 고성에 숨겨진 초대장의 비밀을 풀고 탈출하세요.', 'https://images.unsplash.com/photo-1518005020951-eccb494ad742?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('마법 학교', '금지된 주문서가 사라진 밤, 교실 곳곳의 마법 장치를 해제합니다.', 'https://images.unsplash.com/photo-1529156069898-49953e39b3ac?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('지하 금고', '철통 보안 금고 안에 갇힌 팀이 제한 시간 안에 출구를 찾아야 합니다.', 'https://images.unsplash.com/photo-1519608487953-e999c86e7455?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('해적선의 지도', '저주받은 해적선에서 보물 지도를 완성하고 갑판으로 탈출하세요.', 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('미래 도시', '감시 시스템이 장악한 도시에서 신분 코드를 복구하는 SF 테마입니다.', 'https://commons.wikimedia.org/wiki/Special:FilePath/New%20York%20City%20Skyline%20at%20night.jpg?width=900');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('잃어버린 박물관', '밤이 되면 살아나는 전시실에서 마지막 유물을 찾아야 합니다.', 'https://commons.wikimedia.org/wiki/Special:FilePath/Kelvingrove%20Art%20Gallery%20and%20Museum%20-%20interior%20-%202025-04-17%2001.jpg?width=900');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('기억의 호텔', '투숙객의 사라진 기억을 객실 단서로 복원하는 감성 미스터리입니다.', 'https://images.unsplash.com/photo-1560185127-6ed189bf02f4?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('감옥 탈출', '간수의 순찰이 끝나기 전 감방과 복도의 잠금 장치를 풀어야 합니다.', 'https://images.unsplash.com/photo-1509248961158-e54f6934749c?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('비밀의 정원', '아름다운 정원 속 위험한 퍼즐을 해결하고 숨겨진 문을 여세요.', 'https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('잠수함 SOS', '심해에 멈춘 잠수함의 전력을 복구하고 구조 신호를 송출합니다.', 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('서커스의 밤', '화려한 무대 뒤편에 숨겨진 트릭과 장치를 파헤치는 테마입니다.', 'https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?auto=format&fit=crop&w=900&q=80');
INSERT INTO theme (name, description, thumbnail_url) VALUES
    ('시간 여행자', '뒤엉킨 시간선을 정리하고 원래의 현재로 돌아와야 합니다.', 'https://images.unsplash.com/photo-1501139083538-0139583c060f?auto=format&fit=crop&w=900&q=80');

INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('민준', 1, '2026-04-30', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서연', 1, '2026-04-30', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('도윤', 2, '2026-04-30', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('하윤', 3, '2026-05-01', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지호', 4, '2026-05-01', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지우', 1, '2026-05-01', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('준서', 5, '2026-05-02', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서아', 2, '2026-05-02', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('예준', 6, '2026-05-02', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('유나', 1, '2026-05-02', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('시우', 7, '2026-05-03', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('수아', 3, '2026-05-03', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('연우', 8, '2026-05-03', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('현우', 2, '2026-05-04', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('다은', 9, '2026-05-04', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('은우', 1, '2026-05-04', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('채원', 10, '2026-05-04', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('건우', 4, '2026-05-05', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('유준', 2, '2026-05-05', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지민', 11, '2026-05-05', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('소율', 1, '2026-05-05', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('하준', 3, '2026-05-06', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서준', 12, '2026-05-06', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('나윤', 5, '2026-05-07', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('예린', 2, '2026-05-06', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('주원', 13, '2026-05-07', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('아린', 1, '2026-05-07', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('도현', 6, '2026-05-07', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('윤서', 14, '2026-05-08', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('이준', 2, '2026-05-08', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서윤', 7, '2026-05-08', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('민서', 15, '2026-05-08', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('우진', 1, '2026-05-09', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('가은', 8, '2026-05-09', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('준우', 3, '2026-05-09', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('시윤', 4, '2026-05-10', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('하린', 9, '2026-05-10', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지안', 2, '2026-05-10', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('태오', 10, '2026-05-10', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('라온', 1, '2026-05-11', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서하', 11, '2026-05-11', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('재윤', 5, '2026-05-11', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('이안', 12, '2026-05-12', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('다온', 2, '2026-05-12', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('로운', 13, '2026-05-12', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('소윤', 6, '2026-05-12', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('은서', 14, '2026-05-13', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('현서', 3, '2026-05-13', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지율', 15, '2026-05-13', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('유찬', 1, '2026-05-13', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('태윤', 8, '2026-05-01', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('민재', 8, '2026-05-02', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('하람', 8, '2026-05-04', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('윤재', 8, '2026-05-06', 6);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서진', 9, '2026-05-01', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지유', 9, '2026-05-02', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('이든', 9, '2026-05-05', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('유빈', 10, '2026-05-01', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('시온', 10, '2026-05-03', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('아윤', 11, '2026-05-02', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('도아', 11, '2026-05-04', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('민채', 12, '2026-05-02', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지후', 13, '2026-05-03', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서율', 14, '2026-05-04', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('해준', 1, '2026-05-03', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('예서', 1, '2026-05-06', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지훈', 2, '2026-05-04', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('가온', 2, '2026-05-06', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('리안', 8, '2026-05-05', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('나은', 8, '2026-05-06', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('수현', 9, '2026-05-03', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('도겸', 10, '2026-05-04', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('하율', 11, '2026-05-06', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('정우', 1, '2026-05-01', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('서현', 1, '2026-05-01', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('유건', 2, '2026-05-01', 7);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('다윤', 2, '2026-05-01', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('연서', 8, '2026-05-01', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('도윤', 9, '2026-05-06', 1);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('지아', 9, '2026-05-06', 2);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('하진', 10, '2026-05-06', 3);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('시현', 10, '2026-05-06', 4);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('은채', 11, '2026-05-01', 5);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('예찬', 3, '2026-05-04', 8);
INSERT INTO reservation (username, theme_id, date, time_id) VALUES ('윤하', 4, '2026-05-03', 8);
