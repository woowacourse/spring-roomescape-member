TRUNCATE TABLE member RESTART IDENTITY;

INSERT INTO member(name, email, password, role) VALUES
('테드', 'test1@email.com', '1450575459', 'USER');
