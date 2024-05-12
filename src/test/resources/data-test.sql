INSERT INTO RESERVATION_TIME (ID, START_AT)
VALUES (default, '10:00:00'),
       (default, '12:00:00'),
       (default, '14:00:00');

INSERT INTO THEME (ID, NAME, DESCRIPTION, THUMBNAIL)
VALUES (default, '공포', '완전 무서운 테마', 'https://example.org'),
       (default, '힐링', '완전 힐링되는 테마', 'https://example.org'),
       (default, '힐링2', '완전 힐링되는 테마2', 'https://example.org');

INSERT INTO MEMBER (ID, NAME, EMAIL, PASSWORD)
VALUES (default, '페드로', 'pedro@me.com', '11111'),
       (default, '클로버', 'clover@me.com', '22222'),
       (default, '클로버1', 'clover1@me.com', '22222'),
       (default, '클로버2', 'clover2@me.com', '22222'),
       (default, '클로버3', 'clover3@me.com', '22222'),
       (default, '클로버4', 'clover4@me.com', '22222'),
       (default, '클로버5', 'clover5@me.com', '22222');

INSERT INTO ROLE (MEMBER_ID, ROLE)
VALUES (1, 'ADMIN'),
       (2, 'MEMBER'),
       (3, 'MEMBER'),
       (4, 'MEMBER'),
       (5, 'MEMBER'),
       (6, 'MEMBER'),
       (7, 'MEMBER');

INSERT INTO RESERVATION (ID, MEMBER_ID, DATE, TIME_ID, THEME_ID)
VALUES (default, 1, '2099-12-31', 1, 1),
       (default, 2, '2099-12-31', 1, 2),
       (default, 1, '2024-12-01', 1, 2),
       (default, 2, '2024-12-02', 1, 2),
       (default, 3, '2024-12-02', 2, 2),
       (default, 4, '2024-12-03', 1, 2),
       (default, 5, '2024-12-04', 1, 2);


SELECT * FROM MEMBER;
