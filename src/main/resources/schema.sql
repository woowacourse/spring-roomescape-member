CREATE TABLE theme
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL
);

CREATE TABLE reservation_time
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_at VARCHAR(255) NOT NULL
);

CREATE TABLE reservation
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT,
    theme_id BIGINT,
    FOREIGN KEY (time_id) REFERENCES reservation_time(id),
    FOREIGN KEY (theme_id) REFERENCES theme(id)
);
