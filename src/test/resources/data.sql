INSERT INTO theme (name, description, image_url, running_time)
VALUES ('공포의 병원', '버려진 정신병원에서 탈출해야 합니다.', 'https://picsum.photos/200/300', 60);

INSERT INTO theme (name, description, image_url, running_time)
VALUES ('박물관 침입', '전설의 다이아몬드를 훔쳐 나오세요.', 'https://picsum.photos/200/300', 75);

INSERT INTO theme (name, description, image_url, running_time)
VALUES ('셜록의 서재', '명탐정의 서재 속에 숨겨진 비밀을 찾으세요.', 'https://picsum.photos/200/300', 80);

INSERT INTO theme (name, description, image_url, running_time)
VALUES ('우주선 탈출', '산소가 떨어지기 전에 지구로 귀환해야 합니다.', 'https://picsum.photos/200/300', 90);

INSERT INTO reservation_time (start_at)
VALUES
    ('10:00'),
    ('11:00'),
    ('12:00'),
    ('13:00');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('어셔', '2026-05-04', 1, 2);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('루드비코', '2026-05-05', 2, 2);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('브라운', '2026-05-05', 3, 1);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('코코', '2026-05-06', 1, 1);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('제이슨', '2026-05-06', 2, 2);
