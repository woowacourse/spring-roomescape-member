drop table if exists reservation;
drop table if exists reservation_time;
drop table if exists theme;

create table reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

create table theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255),
    PRIMARY KEY (id)
);

create table reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     VARCHAR(255) NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

insert into theme (name, description, thumbnail)
values ('세렌디피티: 뜻밖의 행운',
        '방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다. 퍼즐은 다양한 형태로 주어질 수 있으며, 팀원들은 상호 협력하여 각종 단서를 찾고 연결하여 문제를 해결해야 합니다. 성공적으로 퍼즐을 풀고 모든 단서를 이용해 탈출하면 게임을 클리어할 수 있습니다.',
        'https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png'),
       ('SOS',
        '플레이어들이 주어진 시간 안에 퍼즐을 해결하고, 숨겨진 단서를 찾아서 탈출하는 것을 목표로 합니다. 대부분의 경우, 게임은 제한된 공간 안에 설정되어 있으며, 플레이어들은 이 공간에서 다양한 퍼즐을 풀어서 출구를 찾아야 합니다.',
        'https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg'),
       ('데이트 코스 연구회',
        '게임의 시작부터 끝까지, 플레이어들은 로맨틱한 분위기를 느끼며 함께 다양한 퍼즐을 풀고 탈출하기 위해 협력해야 합니다. 일반적으로 "데이트 코스 연구회"의 방탈출 게임은 두 사람 이상의 플레이어가 함께 참여하며, 서로 협력하여 문제를 해결해야 합니다.',
        'https://i.postimg.cc/vDFKqct1/theme.jpg'),
       ('탈출: 미래의 시작',
        '미래의 시작은 플레이어들이 미래의 세계에서 퍼즐을 풀고 탈출하는 것을 목표로 합니다. 이러한 방탈출 게임은 플레이어들이 미래의 세계에서 퍼즐을 풀고 탈출하는 것을 목표로 합니다. 이러한 방탈출 게임은 플레이어들이 미래의 세계에서 퍼즐을 풀고 탈출하는 것을 목표로 합니다.',
        'https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg'),
       ('타임 트래블 탈출',
        '참가자들은 시간을 되돌리거나 미래로 뛰어넘어야 하는 도전적인 퍼즐과 과제를 해결하여 과거나 미래에서 벗어나야 합니다.',
        'https://i.postimg.cc/vDFKqct1/theme.jpg'),
       ('미스터리 뮤지엄',
        '참가자들은 박물관에 갇혔는데, 특별한 전시품을 찾아내고 미스터리를 해결하여 탈출해야 합니다.',
        'https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg'),
       ('우주 탈출',
        '우주선이 고장나서 우주 공간에서 갇힌 참가자들은 우주선을 수리하고 지구로 돌아가기 위해 우주 스테이션 안에서 탈출해야 합니다.',
        'https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png'),
       ('고향 마을 탈출',
        '고향 마을에 갇힌 참가자들은 고향 마을을 탈출하기 위해 마을 사람들의 도움을 받아야 합니다.',
        'https://i.postimg.cc/cLqW2JLB/theme-SOS-SOS.jpg'),
       ('포레스트 서바이벌',
        '포레스트에 갇힌 참가자들은 포레스트에서 생존하기 위해 다양한 퍼즐을 풀고 탈출해야 합니다.',
        'https://i.postimg.cc/vDFKqct1/theme.jpg'),
       ('타임 트래블러의 보석',
        '타임 트래블러가 되어 과거와 미래로 여행하며 보석을 찾아야 하는 퍼즐을 풀어 탈출해야 합니다.',
        'https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png');

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
       ('21:00');

insert into reservation (name, date, time_id, theme_id)
values ('홍길동', CURDATE() - 1, 1, 1),
       ('김철수', CURDATE() - 1, 2, 2),
       ('이영희', CURDATE() - 1, 3, 3),
       ('박영수', CURDATE() - 1, 4, 4),
       ('최영희', CURDATE() - 1, 5, 5),
       ('김영수', CURDATE() - 1, 6, 6),
       ('이영수', CURDATE() - 1, 7, 7),
       ('박영희', CURDATE() - 1, 8, 8),
       ('최영수', CURDATE() - 1, 9, 9),
       ('김영희', CURDATE() - 1, 10, 10),
       ('이영수', CURDATE() - 1, 11, 1),
       ('박영희', CURDATE() - 1, 12, 2);
