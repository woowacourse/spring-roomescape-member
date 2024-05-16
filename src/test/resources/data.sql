INSERT INTO theme(name, description, thumbnail)
VALUES ('테마1', '설명1',
        'https://www.urbanbrush.net/web/wp-content/uploads/edd/2022/08/urbanbrush-20220801083851022216.jpg');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마2', '설명2',
        'https://www.urbanbrush.net/web/wp-content/uploads/edd/2022/12/urbanbrush-20221209202526239031.jpg');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마3', '설명3',
        'https://previews.123rf.com/images/yusakp/yusakp1405/yusakp140500013/28097632-%EA%B7%80%EC%97%AC%EC%9A%B4-%EA%BF%80%EB%B2%8C%EC%9D%98-%EB%B2%A1%ED%84%B0-%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8-%EB%A0%88%EC%9D%B4-%EC%85%98.jpg');
INSERT INTO theme(name, description, thumbnail)
VALUES ('테마4', '설명4',
        'https://www.urbanbrush.net/web/wp-content/uploads/edd/2022/12/urbanbrush-20221209202526239031.jpg');

INSERT INTO reservation_time(start_at)
VALUES ('11:59');
INSERT INTO reservation_time(start_at)
VALUES ('17:12');
INSERT INTO reservation_time(start_at)
VALUES ('11:11');
INSERT INTO reservation_time(start_at)
VALUES ('13:11');

INSERT INTO reservation(date, time_id, theme_id)
VALUES (DATEADD('DAY', 1, CURRENT_DATE()), 1, 1);
INSERT INTO reservation(date, time_id, theme_id)
VALUES (DATEADD('YEAR', 1, CURRENT_DATE()), 2, 2);
INSERT INTO reservation(date, time_id, theme_id)
VALUES (DATEADD('DAY', 4, CURRENT_DATE()), 3, 2);

INSERT INTO member(name, email, password, role)
VALUES ('관리자', 'admin@email.com', 'password', 'ADMIN');
INSERT INTO member(name, email, password, role)
VALUES ('멤버', 'member@email.com', 'password', 'MEMBER');

INSERT INTO reservation_list(member_id, reservation_id)
VALUES (2, 1);
INSERT INTO reservation_list(member_id, reservation_id)
VALUES (2, 3);
INSERT INTO reservation_list(member_id, reservation_id)
VALUES (2, 2);
