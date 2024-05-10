INSERT INTO reservation_time (start_at)
VALUES ('10:00:00'), -- 삭제 가능
       ('11:00:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('이름1', '설명1', '썸네일1'), -- 삭제 가능
       ('이름2', '설명2', '썸네일2');

INSERT INTO member (name, email, password, role)
VALUES ('어드민', 'admin@email.com', 'admin', 'ADMIN'),
       ('고객', 'customer@email.com', 'customer', 'CUSTOMER');

INSERT INTO reservation (member_id, date, time_id, theme_id)
VALUES (2, '2024-05-01', 2, 2);
