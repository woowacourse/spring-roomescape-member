DELETE FROM reservation;
DELETE FROM reservation_date;
DELETE FROM reservation_time;
DELETE FROM theme;


INSERT INTO reservation_time (start_at, is_active)
VALUES ('10:00:00', TRUE);
