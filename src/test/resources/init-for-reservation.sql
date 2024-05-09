drop table if exists reservation;
drop table if exists reservation_time;
drop table if exists user_table;
drop table if exists theme;

CREATE TABLE reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE user_table
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    date VARCHAR(255) NOT NULL,
    member_id BIGINT NOT NULL,
    time_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES user_table (id)
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
        'https://i.postimg.cc/vDFKqct1/theme.jpg');

insert into user_table (name, email, password, role)
values ('admin', 'admin', 'admin', 'ADMIN'),
       ('name1', 'email1', 'qq1', 'USER'),
       ('name2', 'email2', 'qq2', 'USER');

insert into reservation_time (start_at)
values ('10:00'),
       ('11:00'),
       ('12:00');
