SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE reservation RESTART IDENTITY;
TRUNCATE TABLE reservation_time RESTART IDENTITY;
TRUNCATE TABLE theme RESTART IDENTITY;
TRUNCATE TABLE registration RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO reservation_time (start_at)
VALUES ('15:40'),
       ('13:40'),
       ('17:40');

INSERT INTO theme (name, description, thumbnail)
VALUES ('polla', '폴라 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('dobby', '도비 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('pobi', '포비 방탈출', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('폴라', '2024-04-30', 1, 1),
       ('구구', '2023-05-01', 2, 1);

INSERT INTO registration (name, email, password)
VALUES ('도비', 'kimdobby@wootaeco.com', 'pass1'),
       ('피케이', 'pke@best.com', 'pass2'),
       ('어드민', 'admin@admin.com', '1234'),
       ('테스트', 'test@test.com', 'test');
