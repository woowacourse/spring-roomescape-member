INSERT INTO theme (name, description, thumbnail_url)
VALUES ('미술관의 밤', '고요한 미술관에서 단서를 모아 탈출하는 추리 테마', 'https://example.com/themes/museum-night.png'),
       ('심해 연구소', '해저 연구소의 사고 원인을 추적하는 SF 테마', 'https://example.com/themes/deep-sea-lab.png'),
       ('폐병원 탈출', '버려진 병원에서 탈출하는 공포 테마', 'https://example.com/themes/hospital.png'),
       ('한밤의 서점', '비밀 통로가 숨겨진 서점에서 단서를 수집하는 미스터리 테마', 'https://example.com/themes/bookstore.png'),
       ('빙하 기지', '얼어붙은 관측 기지에서 생존 루트를 찾는 서바이벌 테마', 'https://example.com/themes/glacier-base.png'),
       ('황금 사원', '사원의 봉인을 해제하고 보물을 찾는 어드벤처 테마', 'https://example.com/themes/golden-temple.png'),
       ('달 기지 탈출', '산소가 부족한 달 기지에서 귀환선을 가동하는 SF 테마', 'https://example.com/themes/moon-base.png'),
       ('무도회장의 유령', '유령이 남긴 암호를 풀어야 하는 고딕 추리 테마', 'https://example.com/themes/ballroom-ghost.png'),
       ('사막 열차', '사막 한가운데 멈춘 열차에서 범인을 찾는 추리 테마', 'https://example.com/themes/desert-train.png'),
       ('해커의 방', '침입당한 서버룸에서 시스템을 복구하는 현대 스릴러 테마', 'https://example.com/themes/hacker-room.png');

INSERT INTO reservation_time (start_at, theme_id)
VALUES ('10:00:00', 1),
       ('11:30:00', 1),
       ('10:00:00', 2),
       ('11:30:00', 2),
       ('10:00:00', 3),
       ('10:00:00', 4),
       ('10:00:00', 5),
       ('10:00:00', 6),
       ('10:00:00', 7),
       ('10:00:00', 8),
       ('10:00:00', 9),
       ('10:00:00', 10);
INSERT INTO reservation (name, date, time_id)
VALUES ('브라운', DATE '2026-05-05', 1),
       ('코니', DATE '2026-05-04', 1),
       ('샐리', DATE '2026-05-03', 2),
       ('문', DATE '2026-05-02', 2),
       ('제시카', DATE '2026-05-01', 1),

       ('제임스', DATE '2026-05-05', 3),
       ('레오', DATE '2026-05-04', 3),
       ('루카', DATE '2026-05-03', 4),
       ('앤디', DATE '2026-05-02', 4),

       ('레너드', DATE '2026-05-05', 5),
       ('초코', DATE '2026-05-04', 5),
       ('브이', DATE '2026-05-03', 5),

       ('에디', DATE '2026-05-05', 6),
       ('리아', DATE '2026-05-04', 6),

       ('마크', DATE '2026-05-05', 7),
       ('니나', DATE '2026-05-04', 7),

       ('제이', DATE '2026-05-05', 8),
       ('하나', DATE '2026-05-05', 9),
       ('오웬', DATE '2026-05-05', 10),
       ('소라', DATE '2026-05-05', 11),
       ('태오', DATE '2026-05-05', 12),

       ('미래예약1', DATE '2026-05-12', 1),
       ('미래예약2', DATE '2026-05-12', 3);
