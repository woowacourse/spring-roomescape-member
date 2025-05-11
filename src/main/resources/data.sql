INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('12:00'),
       ('14:00'),
       ('16:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '설명1',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마2', '설명2',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마3', '설명3',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마4', '설명4',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마5', '설명5',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마6', '설명6',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마7', '설명7',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마8', '설명8',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마9', '설명9',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마10', '설명10',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마11', '설명11',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90'),
       ('테마12', '설명12',
        'https://leisure-web.yanolja.com/_next/image?url=https%3A%2F%2Fimage6.yanolja.com%2Fleisure%2FoK3Zfq3B5N7dbRYz&w=3840&q=90');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('예약1', '2025-04-25', 1, 1),
       ('예약2', '2025-04-25', 2, 3),
       ('예약3', '2025-04-26', 3, 3),
       ('예약4', '2025-04-26', 4, 3),
       ('예약5', '2025-04-27', 1, 5),
       ('예약6', '2025-04-27', 2, 7),
       ('예약7', '2025-04-28', 3, 7),
       ('예약8', '2025-04-28', 4, 10),
       ('예약9', '2025-04-29', 1, 11),
       ('예약10', '2025-04-29', 2, 10),
       ('예약11', '2025-05-01', 3, 11),
       ('예약12', '2025-05-01', 4, 12),
       ('예약11', '2025-04-25', 1, 2),
       ('예약12', '2025-04-25', 2, 2),
       ('예약13', '2025-04-26', 3, 2),
       ('예약14', '2025-04-26', 4, 4),
       ('예약15', '2025-04-27', 1, 5),
       ('예약16', '2025-04-27', 2, 1),
       ('예약17', '2025-04-28', 3, 2),
       ('예약18', '2025-04-28', 4, 8),
       ('예약19', '2025-04-29', 1, 8),
       ('예약20', '2025-04-29', 2, 8),
       ('예약21', '2025-04-29', 3, 11),
       ('예약22', '2025-04-29', 4, 12);


INSERT INTO member (id, name, email, password)
VALUES (1, 'hi', 'hi@hi.hi', '1234');