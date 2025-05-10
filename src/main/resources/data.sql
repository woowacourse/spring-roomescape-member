INSERT INTO theme (id, name, description, thumbnail)
VALUES (1, '안녕, 자두야', '자두', 'https://jado.com');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (2, '안녕, 도기야', '도가', 'https://dogi.com');
INSERT INTO theme (id, name, description, thumbnail)
VALUES (3, '안녕, 젠슨아', '젠슨', 'https://jenson.com');

INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00');
INSERT INTO reservation_time (id, start_at)
VALUES (2, '11:00');
INSERT INTO reservation_time (id, start_at)
VALUES (3, '12:00');
INSERT INTO reservation_time (id, start_at)
VALUES (4, '13:00');

INSERT INTO member (role, name, email, password)
VALUES ('admin', 'jenson', 'a@example.com', 'abc');