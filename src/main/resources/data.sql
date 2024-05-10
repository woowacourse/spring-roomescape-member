INSERT INTO reservation_time (start_at) VALUES ('10:00:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00:00');
INSERT INTO theme (name, description, thumbnail) VALUES ( '공포', '완전 무서운 테마', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg' );
INSERT INTO theme (name, description, thumbnail) VALUES ( '힐링', '완전 힐링되는 테마', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg' );
INSERT INTO theme (name, description, thumbnail) VALUES ( '힐링2', '완전 힐링되는 테마2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '페드로', 'pedro@me.com', '11111' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '클로버', 'clover@me.com', '22222' );

INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 1, '2099-12-31', 1, 1);
INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 2, '2099-12-31', 1, 2);

INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 1, '2024-4-28', 1, 1);
INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 2, '2024-4-28', 1, 2);
INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 1, '2024-4-30', 1, 1);
INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 2, '2024-4-27', 1, 2);
INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 2, '2024-4-30', 1, 2);
INSERT INTO reservation (MEMBER_ID, date, time_id, theme_id) VALUES ( 2, '2024-4-30', 1, 3);
