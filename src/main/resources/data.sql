INSERT INTO RESERVATION_TIME(START_AT)
VALUES ('15:00'),
       ('16:00'),
       ('17:00'),
       ('18:00');

INSERT INTO THEME(NAME, DESCRIPTION, THUMBNAIL)
VALUES ('봄', '설명1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('여름', '설명2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('가을', '설명3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('겨울', '설명4', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO MEMBER(NAME, EMAIL, PASSWORD, ROLE)
VALUES ('레디', 'redddy@gmail.com', '0000', 'ADMIN'),
       ('재즈', 'gkatjraud1@redddybabo.com', '1234', 'USER'),
       ('제제', 'jinwuo0925@gmail.com', '486', 'USER');

INSERT INTO RESERVATION(MEMBER_ID, DATE, TIME_ID, THEME_ID)
VALUES (1, CURRENT_DATE - 3, 1, 1),
       (2, CURRENT_DATE - 2, 3, 2),
       (1, CURRENT_DATE - 1, 2, 2),
       (2, CURRENT_DATE - 1, 1, 2),
       (3, CURRENT_DATE - 7, 1, 3),
       (3, CURRENT_DATE + 3, 4, 3);
