INSERT INTO reservation_time (start_at)
VALUES ('13:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00');
INSERT INTO reservation_time (start_at)
VALUES ('15:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '테마1 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마2', '테마2 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마3', '테마3 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마4', '테마4 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마5', '테마5 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마6', '테마6 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마7', '테마7 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마8', '테마8 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마9', '테마9 설명', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마10', '테마10 설명',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마11', '테마11 설명',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-01', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-02', 2, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-03', 3, 1);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-04', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-05', 2, 2);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-06', 1, 3);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-07', 1, 4);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-08', 1, 5);
INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('홍길동', '2024-05-09', 1, 6);

INSERT INTO member (name, email, password, role)
VALUES ('홍길동', 'hong@gmail.com', '1234', 'MEMBER');
INSERT INTO member (name, email, password, role)
VALUES ('행성이', 'planet@gmail.com', '1111', 'ADMIN');
