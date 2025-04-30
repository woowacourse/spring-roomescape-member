INSERT INTO RESERVATION_TIME(start_at)
VALUES ('13:40'),
       ('14:40'),
       ('15:40')
;

INSERT INTO THEME(name, description, thumbnail)
VALUES ('첫번째', '일번방', '썸네일1'),
       ('두번째', '이번방', '썸네일2'),
       ('세번째', '삼번방', '썸네일3')
;


INSERT INTO RESERVATION(name, date, time_id, theme_id)
VALUES ('브라운', '2023-03-03', '1', '1'),
       ('솔라', '2023-03-03', '2', '2'),
       ('네오', '2023-03-05', '3', '3')
;
