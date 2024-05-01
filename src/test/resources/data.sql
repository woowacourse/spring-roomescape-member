SET REFERENTIAL_INTEGRITY FALSE;
truncate table reservation_time;
truncate table reservation;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO reservation_time (start_at) VALUES ('15:40');
INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');

INSERT INTO theme (name, description, thumbnail) VALUES ('공포', '아아', 'ㅠㅠ');

INSERT INTO reservation (name, date, time_id, theme_id) values ( '아서', '2024-04-24', 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) values ( '몰리', '2024-04-23', 2, 1);
