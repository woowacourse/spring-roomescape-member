DELETE FROM reservation;
DELETE FROM reservation_time;
DELETE FROM theme;
DELETE FROM member;

INSERT INTO member(name, email, password, role)
VALUES
('훌라', 'test@test.com', 'test', 'USER'),
('어드민', 'admin@admin.com', 'admin', 'ADMIN');
