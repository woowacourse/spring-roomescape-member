INSERT INTO reservation_date (date, is_active)
VALUES (DATEADD('DAY', -1, CURRENT_DATE), TRUE);
