INSERT INTO theme (id, name, description, thumbnail_url)
VALUES
    (1, '미술관의 밤', '고요한 미술관에서 단서를 모아 탈출하는 추리 테마', 'https://example.com/themes/museum-night.png'),
    (2, '심해 연구소', '해저 연구소의 사고 원인을 추적하는 SF 테마', 'https://example.com/themes/deep-sea-lab.png'),
    (3, '폐병원 탈출', '버려진 병원에서 탈출하는 공포 테마', 'https://example.com/themes/hospital.png');

INSERT INTO reservation_time (id, start_at, theme_id)
VALUES
    (1, '10:00:00', 1),
    (2, '11:30:00', 1),

    (3, '10:00:00', 2),
    (4, '11:30:00', 2),

    (5, '10:00:00', 3);


INSERT INTO reservation (id, name, date, time_id)
VALUES
    (1, '브라운', DATE '2026-05-10', 1),
    (2, '코니', DATE '2026-05-11', 1),
    (3, '샐리', DATE '2026-05-12', 2),

    (4, '제임스', DATE '2026-05-10', 3),
    (5, '문', DATE '2026-05-11', 4),

    (6, '레너드', DATE '2026-05-10', 5);