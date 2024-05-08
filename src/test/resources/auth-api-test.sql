TRUNCATE TABLE member RESTART IDENTITY;

INSERT INTO member(name, email, password_hash) VALUES
('ted', 'test@email.com', '1450575459');
