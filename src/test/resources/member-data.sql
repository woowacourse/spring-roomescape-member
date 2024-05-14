INSERT INTO role
VALUES (1, 'GUEST'),
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
