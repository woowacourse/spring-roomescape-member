DELETE
FROM reservation;

DELETE
FROM reservation_time;

DELETE
FROM theme;

ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO reservation_time
    (start_at)
VALUES ('10:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('11:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('12:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('13:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('14:00');

INSERT INTO reservation_time
    (start_at)
VALUES ('15:00');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출1', '1번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출2', '2번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출3', '3번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출4', '4번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출5', '5번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출6', '6번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출7', '7번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출8', '8번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출9', '9번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출10', '10번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출11', '11번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출12', '12번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출13', '13번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출14', '14번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO theme
    (name, description, thumbnail)
VALUES ('방탈출15', '15번 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-25', 1, 5);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-25', 2, 5);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-25', 3, 5);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-26', 4, 5);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-26', 5, 5);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-26', 6, 4);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-27', 1, 4);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-27', 2, 4);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-27', 3, 4);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-28', 4, 3);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-28', 5, 3);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-28', 6, 3);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-29', 1, 2);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-29', 2, 2);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-29', 3, 1);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-30', 4, 7);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-30', 5, 8);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-30', 6, 9);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-05-01', 1, 10);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-05-01', 2, 11);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-23', 1, 4);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('다온', '2024-04-23', 2, 4);

