INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '설명', 'https://i.pinimg.com/474x/e2/55/4d/e2554dea5499f88c242178ce7ed568d9.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마2', '설명', 'https://i.pinimg.com/474x/5d/0e/94/5d0e94950f0a5a6f0fea753aaf174da1.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마3', '설명', 'https://i.pinimg.com/474x/3a/72/bd/3a72bd13cefcb1c55ac608705518db9e.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마4', '설명', 'https://i.pinimg.com/474x/5b/99/80/5b99802553149f641bc4fc745cf26ee1.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마5', '설명', 'https://i.pinimg.com/474x/13/2f/c1/132fc1839fdb0afd3e7bf13b5f53098c.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마6', '설명', 'https://i.pinimg.com/474x/63/e6/ba/63e6ba9d54591d35b9178ea65b26dd06.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마7', '설명', 'https://i.pinimg.com/474x/13/82/9e/13829e38b283fc2c1e9250d4aec84351.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마8', '설명', 'https://i.pinimg.com/474x/62/89/86/6289867f34baa0a9708278a6c57ed2c0.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마9', '설명', 'https://i.pinimg.com/474x/67/3f/c3/673fc38a5e8814e2c71251bfe0bc4154.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마10', '설명', 'https://i.pinimg.com/474x/62/a6/1c/62a61c78a2228e23c14fb5b27951c5df.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마11', '설명', 'https://i.pinimg.com/474x/b3/aa/d7/b3aad752a5fbda932dd37015bca3047f.jpg');

INSERT INTO reservation_time (start_at)
VALUES ('10:00');

INSERT INTO member (name, email, password, role)
VALUES ('관리자', 'admin@wooteco.com', '1234', 'ADMIN');
INSERT INTO member (name, email, password, role)
VALUES ('테니', 'tenny@wooteco.com', '1234', 'MEMBER');

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 1);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 1);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 3);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 3);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 4);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 4);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 5);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 5);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 6);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 6);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 7);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 7);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 8);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 8);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 9);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 9);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 10);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 10);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-11', 1, 11);
INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-10', 1, 11);
