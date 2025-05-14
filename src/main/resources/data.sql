INSERT INTO member (name, email, password, role) VALUES ( '유저', 'user@test.com', '1234', 'USER' );
INSERT INTO member (name, email, password, role) VALUES ( '어드민', 'admin@test.com', '1234', 'ADMIN' );

INSERT INTO theme (name, description, thumbnail) VALUES ( 'theme', 'theme', 'thumbnail' );
INSERT INTO theme (name, description, thumbnail) VALUES ( 'theme2', 'theme', 'thumbnail' );
INSERT INTO theme (name, description, thumbnail) VALUES ( 'theme3', 'theme', 'thumbnail' );

INSERT INTO reservation_time (start_at) VALUES ( '10:00' );
INSERT INTO reservation_time (start_at) VALUES ( '11:00' );

-- 과거 예약 (theme3 - 2개, theme2 - 7개, theme1 - 3개)
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-12', 1, 1 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-12', 1, 2 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 2, '2025-05-12', 2, 2 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-11', 1, 2 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-11', 2, 2 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-11', 1, 3 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-11', 2, 3 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-10', 1, 1 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-10', 2, 1 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-10', 1, 2 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-10', 2, 2 );
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2025-05-09', 1, 2 );
