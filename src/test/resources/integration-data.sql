INSERT INTO member (id, email, password, name, role)
VALUES (1, 'admin@gmail.com', '$2a$10$MbGFqyn/u4wfggRK7HAqDeC1y9s1mESgmXV3b7e7GZT5u1JkIT.gm', '어드민',
        'ADMIN'), -- password: abc123
       (2, 'alstn1@gmail.com', '1234', '구름1', 'USER'),
       (3, 'alstn2@gmail.com', '1234', '구름2', 'USER'),
       (4, 'alstn3@gmail.com', '1234', '구름3', 'USER'),
       (5, 'alstn4@gmail.com', '1234', '구름4', 'USER');

INSERT INTO theme (id, name, description, thumbnail)
VALUES (1, '고풍 한옥 마을', '한국의 전통적인 아름다움이 당신을 맞이합니다.', 'https://via.placeholder.com/150/92c952'),
       (2, '우주 탐험', '끝없는 우주에 숨겨진 비밀을 파헤치세요.', 'https://via.placeholder.com/150/771796'),
       (3, '시간여행', '과거와 미래를 오가며 역사의 비밀을 밝혀보세요.', 'https://via.placeholder.com/150/24f355'),
       (4, '마법의 숲', '요정과 마법사들이 사는 신비로운 숲 속으로!', 'https://via.placeholder.com/150/30f9e7'),
       (5, '타임캡슐', '오래된 타임캡슐을 찾아내어 그 안의 비밀을 풀어보세요.', 'https://via.placeholder.com/150/56a8c2'),
       (6, '로맨틱 유럽 여행', '로맨틱한 분위기 속에서 유럽을 여행하세요.', 'https://via.placeholder.com/150/7472e7'),
       (7, '신화 속의 세계', '신화와 전설 속으로 당신을 초대합니다.', 'https://via.placeholder.com/150/24f355'),
       (8, '바다 속 신비', '깊은 바다에서의 모험을 경험하세요.', 'https://via.placeholder.com/150/56a8c2');

INSERT INTO reservation_time (id, start_at)
VALUES (1, '09:00'),
       (2, '12:00'),
       (3, '17:00'),
       (4, '21:00');

INSERT INTO reservation (id, date, member_id, time_id, theme_id)
VALUES (1, '2024-04-28', 1, 1, 5),
       (2, '2024-04-28', 2, 2, 5),
       (3, '2024-04-28', 3, 3, 5),
       (4, '2024-04-29', 4, 1, 5),
       (5, '2024-04-29', 1, 2, 5),
       (6, '2024-04-29', 2, 1, 4),
       (7, '2024-04-29', 3, 2, 4),
       (8, '2024-04-29', 4, 3, 4),
       (9, '2024-04-29', 1, 4, 4),
       (10, '2024-05-01', 2, 1, 3),
       (11, '2024-05-01', 3, 2, 3),
       (12, '2024-05-01', 4, 3, 3),
       (13, '2024-05-02', 1, 1, 2),
       (14, '2024-05-02', 2, 2, 2),
       (15, '2024-05-02', 3, 1, 1),
       (16, '2024-05-03', 4, 2, 1),
       (17, '2024-05-03', 1, 3, 1),
       (18, '2024-05-03', 2, 4, 1),
       (19, '2024-05-04', 3, 1, 5),
       (20, '2024-05-04', 4, 2, 4),
       (21, '2024-05-04', 1, 3, 3),
       (22, '2024-05-04', 2, 4, 2);
