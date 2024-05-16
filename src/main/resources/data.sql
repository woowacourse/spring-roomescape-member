INSERT INTO role (name)
VALUES ('GUEST'),
       ('MEMBER'),
       ('ADMIN');

INSERT INTO member (name, role_id)
VALUES ('admin', 3),
       ('naknak', 2),
       ('tre', 2);

INSERT INTO member_credential (member_id, email, password)
VALUES (1, 'admin@example.com', 'woowa123!'),
       (2, 'naknak@example.com', 'nak123'),
       (3, 'tre@example.com', 'iamcute');
