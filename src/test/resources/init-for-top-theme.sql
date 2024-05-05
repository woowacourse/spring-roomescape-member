drop table if exists reservation;
drop table if exists reservation_time;
drop table if exists theme;

create table reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

create table theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255),
    PRIMARY KEY (id)
);

create table reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

insert into theme (name, description, thumbnail)
values  ('name1',
        'description1',
        'thumbnail1'),
        ('name2',
        'description2',
        'thumbnail2'),
        ('name3',
        'description3',
        'thumbnail3'),
        ('name4',
        'description4',
        'thumbnail4'),
        ('name5',
        'description5',
        'thumbnail5'),
        ('name6',
        'description6',
        'thumbnail6'),
        ('name7',
        'description7',
        'thumbnail7'),
        ('name8',
        'description8',
        'thumbnail8'),
        ('name9',
        'description9',
        'thumbnail9'),
        ('name10',
        'description10',
        'thumbnail10'),
        ('name11',
        'description11',
        'thumbnail11'),
        ('name12',
        'description12',
        'thumbnail12'),
        ('name13',
        'description13',
        'thumbnail13');

insert into reservation_time (start_at)
values ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00'),
       ('14:00'),
       ('15:00'),
       ('16:00'),
       ('17:00'),
       ('18:00'),
       ('19:00'),
       ('20:00'),
       ('21:00'),
       ('22:00');

insert into reservation (name, date, time_id, theme_id)
values ('사람1', DATEADD(DAY, -5, CURRENT_DATE), 1, 1),
       ('사람2', DATEADD(DAY, -5, CURRENT_DATE), 2, 1),
       ('사람3', DATEADD(DAY, -5, CURRENT_DATE), 3, 1),
       ('사람4', DATEADD(DAY, -5, CURRENT_DATE), 4, 1),
       ('사람5', DATEADD(DAY, -5, CURRENT_DATE), 1, 2),
       ('사람6', DATEADD(DAY, -5, CURRENT_DATE), 2, 2),
       ('사람7', DATEADD(DAY, -5, CURRENT_DATE), 3, 2),
       ('사람8', DATEADD(DAY, -5, CURRENT_DATE), 1, 3),
       ('사람9', DATEADD(DAY, -5, CURRENT_DATE), 2, 3),
       ('사람14', DATEADD(DAY, -5, CURRENT_DATE), 1, 4),
       ('사람15', DATEADD(DAY, -5, CURRENT_DATE), 1, 5),
       ('사람10', DATEADD(DAY, -5, CURRENT_DATE), 1, 6),
       ('사람11', DATEADD(DAY, -5, CURRENT_DATE), 1, 7),
       ('사람12', DATEADD(DAY, -5, CURRENT_DATE), 1, 8),
       ('사람13', DATEADD(DAY, -5, CURRENT_DATE), 1, 9);
