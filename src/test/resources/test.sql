INSERT INTO reservation_time (start_at)
VALUES ('10:00');
INSERT INTO reservation_time (start_at)
VALUES ('12:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00');
INSERT INTO reservation_time (start_at)
VALUES ('16:00');
INSERT INTO reservation_time (start_at)
VALUES ('18:00');
INSERT INTO reservation_time (start_at)
VALUES ('20:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('인터스텔라', '시공간을 넘나들며 인류의 미래를 구해야 하는 극한의 두뇌 미션, 인터스텔라 방탈출!',
        'https://upload.wikimedia.org/wikipedia/ko/b/b7/%EC%9D%B8%ED%84%B0%EC%8A%A4%ED%85%94%EB%9D%BC.jpg?20150905075839');
INSERT INTO theme (name, description, thumbnail)
VALUES ('라라랜드', '화려한 꿈과 음악 속에서 단서를 찾아라, 라라랜드 감성의 로맨틱 방탈출!',
        'https://upload.wikimedia.org/wikipedia/ko/8/8f/%EB%9D%BC%EB%9D%BC%EB%9E%9C%EB%93%9C_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('컨저링', '실화 기반의 공포가 현실이 된다, 악령이 도사린 집에서 탈출하라!',
        'https://upload.wikimedia.org/wikipedia/ko/c/c6/%EC%BB%A8%EC%A0%80%EB%A7%81.jpg');

-- 2 -> 1 -> 3

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('예약1', '2025-04-24', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('예약2', '2025-04-25', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('예약3', '2025-04-26', 2, 2);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('예약4', '2025-04-24', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('예약5', '2025-04-25', 4, 1);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('예약6', '2025-04-25', 4, 3);
