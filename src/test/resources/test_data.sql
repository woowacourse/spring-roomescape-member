INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('잠실 캠퍼스 탈출',
        '미션을 빨리 진행하고 잠실 캠퍼스를 탈출하자!',
        'https://velog.velcdn.com/images/jangws/post/cfe0e548-1242-470d-bfa8-19eeb72debc5/image.jpg'),
       ('선릉 캠퍼스 탈출',
        '미션을 빨리 진행하고 선릉 캠퍼스를 탈출하자!',
        'https://techblog.woowahan.com/wp-content/uploads/2022/06/iOS-%EC%9D%B4%EB%AF%B8%EC%A7%80.jpg');

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('리비', TIMESTAMPADD(DAY,1, NOW()), 1, 1),
       ('도도', TIMESTAMPADD(DAY,1, NOW()), 2, 1),
       ('썬', TIMESTAMPADD(DAY,1, NOW()), 1, 2);
