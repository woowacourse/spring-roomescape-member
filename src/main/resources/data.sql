INSERT INTO reservation_time (start_at) VALUES ('10:00:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00:00');
INSERT INTO theme (name, description, thumbnail) VALUES ( '공포', '완전 무서운 테마', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg' );
INSERT INTO theme (name, description, thumbnail) VALUES ( '힐링', '완전 힐링되는 테마', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg' );
INSERT INTO theme (name, description, thumbnail) VALUES ( '힐링2', '완전 힐링되는 테마2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg' );
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '페드로', '2099-12-31', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '클로버', '2099-12-31', 1, 2);

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '페드로', '2024-4-28', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '클로버', '2024-4-28', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '페드로', '2024-4-30', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '클로버', '2024-4-27', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '클로버', '2024-4-30', 1, 2);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ( '클로버', '2024-4-30', 1, 3);
