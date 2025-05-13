INSERT INTO THEME (NAME, DESCRIPTION, THUMBNAIL)
VALUES ('name1', 'description1', 'thumbnail1'),
       ('name2', 'description2', 'thumbnail2'),
       ('name3', 'description3', 'thumbnail3'),
       ('name4', 'description4', 'thumbnail4'),
       ('name5', 'description5', 'thumbnail5'),
       ('name6', 'description6', 'thumbnail6'),
       ('name7', 'description7', 'thumbnail7'),
       ('name8', 'description8', 'thumbnail8'),
       ('name9', 'description9', 'thumbnail9'),
       ('name10', 'description10', 'thumbnail10'),
       ('name11', 'description11', 'thumbnail11');

INSERT INTO RESERVATION_TIME (START_AT)
VALUES ('10:00');

INSERT INTO MEMBER (NAME, EMAIL, PASSWORD, ROLE)
VALUES ('테스트1', 'test1@email.com', 'password1', 'ADMIN'),
       ('테스트2', 'test2@email.com', 'password2', 'ADMIN'),
       ('테스트3', 'test3@email.com', 'password3', 'ADMIN');


INSERT INTO RESERVATION (DATE, TIME_ID, THEME_ID, MEMBER_ID)
VALUES ('2025-01-01', 1, 1, 1),
       ('2025-01-02', 1, 1, 2),
       ('2025-01-03', 1, 2, 1),
       ('2025-01-04', 1, 2, 2),
       ('2025-01-05', 1, 3, 3),
       ('2025-02-01', 1, 3, 1),
       ('2025-02-02', 1, 4, 2),
       ('2025-02-03', 1, 4, 3),
       ('2025-02-04', 1, 5, 1),
       ('2025-02-05', 1, 5, 2);
