INSERT INTO theme (name, description, thumbnail)
VALUES ('Nature', 'Beautiful landscapes', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Sports', 'Various sports activities', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Food', 'Delicious food from around the world', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Travel', 'Exciting travel destinations', 'https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg'),
       ('Music', 'Different music genres','https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg');

INSERT INTO reservation_time (start_at)
VALUES ('00:00'),
       ('01:00'),
       ('02:00'),
       ('03:00'),
       ('04:00');

INSERT INTO member (name, email, password, role)
VALUES ('어드민 이름', 'admin@email.com', '1234', 'ADMIN'),
       ('유저 이름', 'user@email.com', '1234', 'USER');
