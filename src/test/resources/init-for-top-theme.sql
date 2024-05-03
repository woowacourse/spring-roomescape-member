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
values ('사람1', '2024-04-29', 1, 1),
       ('사람2', '2024-04-29', 2, 1),
       ('사람3', '2024-04-29', 3, 1),
       ('사람4', '2024-04-29', 4, 1),
       ('사람5', '2024-04-29', 1, 2),
       ('사람6', '2024-04-29', 2, 2),
       ('사람7', '2024-04-29', 3, 2),
       ('사람8', '2024-04-29', 1, 3),
       ('사람9', '2024-04-29', 2, 3),
       ('사람14', '2024-04-29', 1, 4),
       ('사람15', '2024-04-29', 1, 5),
       ('사람10', '2024-04-29', 1, 6),
       ('사람11', '2024-04-29', 1, 7),
       ('사람12', '2024-04-29', 1, 8),
       ('사람13', '2024-04-29', 1, 9);
