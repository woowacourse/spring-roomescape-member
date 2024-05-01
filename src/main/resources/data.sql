INSERT INTO reservation_time(start_at) VALUES '01:00';
INSERT INTO reservation_time(start_at) VALUES '03:00';
INSERT INTO reservation_time(start_at) VALUES '05:00';
INSERT INTO reservation_time(start_at) VALUES '07:00';
INSERT INTO reservation_time(start_at) VALUES '09:00';

INSERT INTO theme(name, description, thumbnail) VALUES('test1', 'desc1', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test2', 'desc2', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test3', 'desc3', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
INSERT INTO theme(name, description, thumbnail) VALUES('test4', 'desc4', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name1', '2024-04-24', '1', '1');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name2', '2024-04-25', '2', '2');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name3', '2024-04-26', '1', '2');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name4', '2024-04-27', '5', '1');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name4', '2024-04-27', '5', '2');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name1', '2024-04-28', '1', '3');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name1', '2024-04-29', '2', '3');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name1', '2024-04-30', '3', '3');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name1', '2024-05-01', '4', '4');
INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('name1', '2024-04-28', '5', '3');
