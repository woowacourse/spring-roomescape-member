CREATE TABLE theme (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    image VARCHAR(255),

    PRIMARY KEY (id)
);

CREATE TABLE schedule (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    theme_id BIGINT NOT NULL,
    date    VARCHAR(255) NOT NULL,
    start_at VARCHAR(255) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

CREATE TABLE reservation_time (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE reservation (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    date    VARCHAR(255) NOT NULL,
    time_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id)
);

INSERT INTO theme (name, description, image) VALUES ('은하수', '은하수 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('지구', '지구 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('수성', '수성 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('금성', '금성 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('화성', '화성 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('목성', '목성 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('토성', '토성 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('보이저', '보이저 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('아폴로', '아폴로 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('허블', '허블 테마방입니다.', 'http.jpg');
INSERT INTO theme (name, description, image) VALUES ('안드로메다', '안드로메다 테마방입니다.', 'http.jpg');

INSERT INTO schedule (theme_id, date, start_at, is_available) VALUES
(1, '2026-05-05', '10:00', TRUE),
(1, '2026-05-05', '11:00', TRUE),
(1, '2026-05-05', '12:00', TRUE),
(1, '2026-05-05', '13:00', TRUE),
(1, '2026-05-05', '14:00', TRUE),
(1, '2026-05-05', '15:00', TRUE),
(1, '2026-05-05', '16:00', TRUE),
(1, '2026-05-05', '17:00', TRUE),
(1, '2026-05-05', '18:00', TRUE),
(1, '2026-05-05', '19:00', TRUE),
(1, '2026-05-05', '20:00', TRUE),

(2, '2026-05-05', '10:00', TRUE),
(2, '2026-05-05', '11:00', TRUE),
(2, '2026-05-05', '12:00', TRUE),
(2, '2026-05-05', '13:00', TRUE),
(2, '2026-05-05', '14:00', TRUE),
(2, '2026-05-05', '15:00', TRUE),
(2, '2026-05-05', '16:00', TRUE),
(2, '2026-05-05', '17:00', TRUE),
(2, '2026-05-05', '18:00', TRUE),
(2, '2026-05-05', '19:00', TRUE),
(2, '2026-05-05', '20:00', TRUE),

(3, '2026-05-05', '10:00', TRUE),
(3, '2026-05-05', '11:00', TRUE),
(3, '2026-05-05', '12:00', TRUE),
(3, '2026-05-05', '13:00', TRUE),
(3, '2026-05-05', '14:00', TRUE),
(3, '2026-05-05', '15:00', TRUE),
(3, '2026-05-05', '16:00', TRUE),
(3, '2026-05-05', '17:00', TRUE),
(3, '2026-05-05', '18:00', TRUE),
(3, '2026-05-05', '19:00', TRUE),
(3, '2026-05-05', '20:00', TRUE),

(4, '2026-05-05', '10:00', TRUE),
(4, '2026-05-05', '11:00', TRUE),
(4, '2026-05-05', '12:00', TRUE),
(4, '2026-05-05', '13:00', TRUE),
(4, '2026-05-05', '14:00', TRUE),
(4, '2026-05-05', '15:00', TRUE),
(4, '2026-05-05', '16:00', TRUE),
(4, '2026-05-05', '17:00', TRUE),
(4, '2026-05-05', '18:00', TRUE),
(4, '2026-05-05', '19:00', TRUE),
(4, '2026-05-05', '20:00', TRUE),

(5, '2026-05-05', '10:00', TRUE),
(5, '2026-05-05', '11:00', TRUE),
(5, '2026-05-05', '12:00', TRUE),
(5, '2026-05-05', '13:00', TRUE),
(5, '2026-05-05', '14:00', TRUE),
(5, '2026-05-05', '15:00', TRUE),
(5, '2026-05-05', '16:00', TRUE),
(5, '2026-05-05', '17:00', TRUE),
(5, '2026-05-05', '18:00', TRUE),
(5, '2026-05-05', '19:00', TRUE),
(5, '2026-05-05', '20:00', TRUE),

(6, '2026-05-05', '10:00', TRUE),
(6, '2026-05-05', '11:00', TRUE),
(6, '2026-05-05', '12:00', TRUE),
(6, '2026-05-05', '13:00', TRUE),
(6, '2026-05-05', '14:00', TRUE),
(6, '2026-05-05', '15:00', TRUE),
(6, '2026-05-05', '16:00', TRUE),
(6, '2026-05-05', '17:00', TRUE),
(6, '2026-05-05', '18:00', TRUE),
(6, '2026-05-05', '19:00', TRUE),
(6, '2026-05-05', '20:00', TRUE),

(7, '2026-05-05', '10:00', TRUE),
(7, '2026-05-05', '11:00', TRUE),
(7, '2026-05-05', '12:00', TRUE),
(7, '2026-05-05', '13:00', TRUE),
(7, '2026-05-05', '14:00', TRUE),
(7, '2026-05-05', '15:00', TRUE),
(7, '2026-05-05', '16:00', TRUE),
(7, '2026-05-05', '17:00', TRUE),
(7, '2026-05-05', '18:00', TRUE),
(7, '2026-05-05', '19:00', TRUE),
(7, '2026-05-05', '20:00', TRUE),

(8, '2026-05-05', '10:00', TRUE),
(8, '2026-05-05', '11:00', TRUE),
(8, '2026-05-05', '12:00', TRUE),
(8, '2026-05-05', '13:00', TRUE),
(8, '2026-05-05', '14:00', TRUE),
(8, '2026-05-05', '15:00', TRUE),
(8, '2026-05-05', '16:00', TRUE),
(8, '2026-05-05', '17:00', TRUE),
(8, '2026-05-05', '18:00', TRUE),
(8, '2026-05-05', '19:00', TRUE),
(8, '2026-05-05', '20:00', TRUE),

(9, '2026-05-05', '10:00', TRUE),
(9, '2026-05-05', '11:00', TRUE),
(9, '2026-05-05', '12:00', TRUE),
(9, '2026-05-05', '13:00', TRUE),
(9, '2026-05-05', '14:00', TRUE),
(9, '2026-05-05', '15:00', TRUE),
(9, '2026-05-05', '16:00', TRUE),
(9, '2026-05-05', '17:00', TRUE),
(9, '2026-05-05', '18:00', TRUE),
(9, '2026-05-05', '19:00', TRUE),
(9, '2026-05-05', '20:00', TRUE),

(10, '2026-05-05', '10:00', TRUE),
(10, '2026-05-05', '11:00', TRUE),
(10, '2026-05-05', '12:00', TRUE),
(10, '2026-05-05', '13:00', TRUE),
(10, '2026-05-05', '14:00', TRUE),
(10, '2026-05-05', '15:00', TRUE),
(10, '2026-05-05', '16:00', TRUE),
(10, '2026-05-05', '17:00', TRUE),
(10, '2026-05-05', '18:00', TRUE),
(10, '2026-05-05', '19:00', TRUE),
(10, '2026-05-05', '20:00', TRUE),

(11, '2026-05-05', '10:00', TRUE),
(11, '2026-05-05', '11:00', TRUE),
(11, '2026-05-05', '12:00', TRUE),
(11, '2026-05-05', '13:00', TRUE),
(11, '2026-05-05', '14:00', TRUE),
(11, '2026-05-05', '15:00', TRUE),
(11, '2026-05-05', '16:00', TRUE),
(11, '2026-05-05', '17:00', TRUE),
(11, '2026-05-05', '18:00', TRUE),
(11, '2026-05-05', '19:00', TRUE),
(11, '2026-05-05', '20:00', TRUE);
