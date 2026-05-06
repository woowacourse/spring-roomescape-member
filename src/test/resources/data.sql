
INSERT INTO themes (id, name, description, thumbnail) VALUES
  (1, 'Theme A', 'Desc A', 'https://example.com/a.png'),
  (2, 'Theme B', 'Desc B', 'https://example.com/b.png'),
  (3, 'Theme C', 'Desc C', 'https://example.com/c.png');

INSERT INTO reservation_time (id, start_at) VALUES
  (1, '10:00:00'),
  (2, '11:00:00'),
  (3, '12:00:00'),
  (4, '13:00:00');

INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES
  (1, 'User1', '2026-05-01', 1, 1),
  (2, 'User2', '2026-05-02', 2, 1),
  (3, 'User3', '2026-05-03', 3, 1),
  (4, 'User4', '2026-05-04', 4, 1),
  (5, 'User5', '2026-05-05', 1, 1);


INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES
  (6, 'User6', '2026-05-01', 2, 2),
  (7, 'User7', '2026-05-02', 3, 2),
  (8, 'User8', '2026-05-06', 4, 2);

INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES
  (9, 'User9', '2026-05-06', 1, 3);

