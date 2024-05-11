DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;
DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS reservation_time (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS theme (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS member (
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS member_email ON member(email);

CREATE TABLE IF NOT EXISTS reservation (
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    member_id BIGINT,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);


INSERT INTO reservation_time (start_at) VALUES ('10:00:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00:00');

INSERT INTO theme (name, description, thumbnail) VALUES ( '공포', '완전 무서운 테마', 'https://example.org' );
INSERT INTO theme (name, description, thumbnail) VALUES ( '힐링', '완전 힐링되는 테마', 'https://example.org' );
INSERT INTO theme (name, description, thumbnail) VALUES ( '힐링2', '완전 힐링되는 테마2', 'https://example.org' );

INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '페드로', 'pedro@me.com', '11111' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '클로버', 'clover@me.com', '22222' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '클로버1', 'clover1@me.com', '22222' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '클로버2', 'clover2@me.com', '22222' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '클로버3', 'clover3@me.com', '22222' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '클로버4', 'clover4@me.com', '22222' );
INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES ( '클로버5', 'clover5@me.com', '22222' );

INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2099-12-31', 1, 1);
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 2, '2099-12-31', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 1, '2024-12-01', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 2, '2024-12-02', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 3, '2024-12-02', 2, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 4, '2024-12-03', 1, 2);
INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES ( 5, '2024-12-04', 1, 2);
