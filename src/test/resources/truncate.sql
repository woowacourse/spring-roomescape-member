DELETE
FROM reservation;
DELETE
FROM reservation_time;
DELETE
FROM reservation;
DELETE
FROM member;
DELETE
FROM theme;

ALTER TABLE theme
    ALTER COLUMN id RESTART;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART;
ALTER TABLE reservation
    ALTER COLUMN id RESTART;
ALTER TABLE member
    ALTER COLUMN id RESTART;

