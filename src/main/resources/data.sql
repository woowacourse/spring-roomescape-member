-- 로컬 실행용 초기 데이터
INSERT INTO reservation_time (start_at)
VALUES ('10:00:00'),
       ('14:00:00');

INSERT INTO theme (name, description, image_url)
VALUES ('웨스턴', '서부 시대 미국 배경 추리 테마입니다.', 'https://i.namu.wiki/i/A1AtvH502V57OxN_IuPqwui9jFHsjBZ18IFkFoBvHfBHSfGDYN9yFmARz6AlyM9AYJDhK1aiqnY5BcVIdWHFcA.webp'),
       ('비밀의 화원', '잊혀진 정원 속 비밀을 풀어나가는 미스터리 테마입니다.', 'https://i.namu.wiki/i/OoI83MDV7W2tuNPf3NCSQADLcng4cRTqQ15nP6JEatDQniUxC800zbwzYBqq2TOE3KhFKXy140SpWfl6uL2d5A.webp');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('브라운', '2026-05-03', 1, 1),
       ('검프', '2026-05-04', 2, 1),
       ('제이슨', '2026-05-05', 1, 2);
