INSERT INTO member (id, name, email, password, role) VALUES ( 1, '유저', 'user@test.com', '1234', 'USER' );
INSERT INTO member (id, name, email, password, role) VALUES ( 2, '어드민', 'admin@test.com', '1234', 'ADMIN' );

INSERT INTO theme (id, name, description, thumbnail) VALUES ( 1, 'theme', 'theme', 'thumbnail' );
INSERT INTO theme (id, name, description, thumbnail) VALUES ( 2, 'theme2', 'theme', 'thumbnail' );
INSERT INTO theme (id, name, description, thumbnail) VALUES ( 3, 'theme3', 'theme', 'thumbnail' );

INSERT INTO reservation_time (id, start_at) VALUES ( 1, '10:00' );
INSERT INTO reservation_time (id, start_at) VALUES ( 2, '11:00' );

-- 과거 예약 (theme3 - 2개, theme2 - 7개, theme1 - 3개)
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 1, 1, '2025-05-12', 1, 1 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 2, 1, '2025-05-12', 1, 2 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 3, 2, '2025-05-12', 2, 2 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 4, 1, '2025-05-11', 1, 2 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 5, 1, '2025-05-11', 2, 2 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 6, 1, '2025-05-11', 1, 3 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 7, 1, '2025-05-11', 2, 3 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 8, 1, '2025-05-10', 1, 1 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 9, 1, '2025-05-10', 2, 1 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 10, 1, '2025-05-10', 1, 2 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 11, 1, '2025-05-10', 2, 2 );
INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( 12, 1, '2025-05-09', 1, 2 );
