-- 로컬 실행용 초기 데이터
INSERT INTO reservation_time (id, start_at)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', '10:00:00'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02', '14:00:00');

INSERT INTO theme (id, name, description, image_url)
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01', '웨스턴', '서부 시대 미국 배경 추리 테마입니다.', 'https://i.namu.wiki/i/A1AtvH502V57OxN_IuPqwui9jFHsjBZ18IFkFoBvHfBHSfGDYN9yFmARz6AlyM9AYJDhK1aiqnY5BcVIdWHFcA.webp'),
       ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb02', '비밀의 화원', '잊혀진 정원 속 비밀을 풀어나가는 미스터리 테마입니다.', 'https://i.namu.wiki/i/OoI83MDV7W2tuNPf3NCSQADLcng4cRTqQ15nP6JEatDQniUxC800zbwzYBqq2TOE3KhFKXy140SpWfl6uL2d5A.webp');

INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES ('cccccccc-cccc-cccc-cccc-cccccccccc01', '브라운', '2026-05-08', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01'),
       ('cccccccc-cccc-cccc-cccc-cccccccccc02', '검프', '2026-05-09', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01'),
       ('cccccccc-cccc-cccc-cccc-cccccccccc03', '제이슨', '2026-05-10', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb02');
