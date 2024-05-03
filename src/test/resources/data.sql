INSERT INTO RESERVATION_TIME (START_AT)
VALUES ('10:15'),
       ('11:20'),
       ('12:25'),
       ('13:30');

INSERT INTO THEME (NAME, DESCRIPTION, THUMBNAIL)
VALUES ('Spring',
        'A time of renewal and growth, as nature awakens from its slumber and bursts forth with vibrant colors and fragrant blooms.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Summer',
        'A season of warmth and abundance, where days stretch long under the sun''s golden gaze, inviting adventures and lazy afternoons by the water.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Autumn',
        'A season of transition, where the world transforms into a canvas of fiery hues, marking the harvest''s bounty and the earth''s gentle preparation for the coming winter slumber.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('Winter',
        'A tranquil realm of snow-covered landscapes and cozy hearths, where the world slows down and finds beauty in the quiet stillness.',
        'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO RESERVATION (NAME, DATE, TIME_ID, THEME_ID)
VALUES ('Seyang', CURRENT_DATE - 8, 1, 1),
       ('Redddy', CURRENT_DATE - 7, 1, 1),
       ('Seyang', CURRENT_DATE - 7, 2, 1),
       ('Redddy', CURRENT_DATE - 7, 3, 1),
       ('Seyang', CURRENT_DATE - 1, 1, 2),
       ('Redddy', CURRENT_DATE - 1, 2, 2),
       ('Seyang', CURRENT_DATE - 1, 3, 2),
       ('Redddy', CURRENT_DATE, 1, 1),
       ('Seyang', CURRENT_DATE, 1, 2),
       ('Redddy', CURRENT_DATE + 1, 1, 1),
       ('Seyang', CURRENT_DATE + 1, 2, 1),
       ('Redddy', CURRENT_DATE + 1, 3, 1);
