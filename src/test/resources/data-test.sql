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

INSERT INTO RESERVATION (NAME, DATE, TIME_ID, THEME_ID)
VALUES ('name', '2025-01-01', 1, 1);

INSERT INTO MEMBER (NAME, EMAIL, PASSWORD, ROLE)
VALUES ('테스트', 'test@email.com', 'password', 'ADMIN');
