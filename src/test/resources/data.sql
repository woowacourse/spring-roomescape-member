INSERT INTO reservation_time (start_at)
VALUES ('10:00:00'), -- 삭제 가능
       ('11:00:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('이름1', '설명1', '썸네일1'), -- 삭제 가능
       ('이름2', '설명2', '썸네일2');

INSERT INTO member (name, email, password, role)
VALUES ('멤버1', '이메일1', '패스워드1', 'ADMIN'), -- 삭제 가능
       ('멤버2', '이메일2', '패스워드2', 'ADMIN');

INSERT INTO reservation (member_id, date, time_id, theme_id, created_at)
VALUES (2, FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), 2, 2, TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()));
