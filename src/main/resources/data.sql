INSERT INTO theme (name, description, thumbnail)
VALUES ('Nature', 'Beautiful landscapes', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Sports', 'Various sports activities', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Food', 'Delicious food from around the world', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Travel', 'Exciting travel destinations', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Music', 'Different music genres','https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg');

INSERT INTO reservation_time (start_at)
VALUES ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00'),
       ('14:00');

INSERT INTO member (name, email, password, role)
VALUES ('어드민 이름', 'admin@email.com', '1234', 'ADMIN'),
       ('유저 이름', 'user@email.com', '1234', 'USER');

INSERT INTO reservation (date, time_id, theme_id, member_id)
VALUES ('2024-06-02', 1, 1, 1),
       ('2024-06-03', 1, 1, 2),
       ('2024-06-04', 1, 1, 2);
