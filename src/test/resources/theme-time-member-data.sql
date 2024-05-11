INSERT INTO theme
VALUES (1, 'theme1', 'description1', 'thumbnail1'),
       (2, 'theme2', 'description2', 'thumbnail2');

INSERT INTO reservation_time
VALUES (1, '10:00'),
       (2, '11:00');

INSERT INTO role
VALUES (1, 'NONE'),
       (2, 'MEMBER'),
       (3, 'ADMIN');

INSERT INTO member
VALUES (1, 'admin', 3),
       (2, 'naknak', 2),
       (3, 'tre', 2);

INSERT INTO member_credential
VALUES (1, 1, 'admin@example.com', 'woowa123!'),
       (2, 2, 'naknak@example.com', 'nak123'),
       (3, 3, 'tre@example.com', 'iamcute');
