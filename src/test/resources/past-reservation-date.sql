DELETE FROM reservation;
DELETE FROM reservation_date;
DELETE FROM reservation_time;
DELETE FROM theme;


INSERT INTO reservation_date (date, is_active)
VALUES (DATEADD('DAY', -1, CURRENT_DATE), TRUE);
