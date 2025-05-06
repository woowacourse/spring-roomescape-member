-- reservation_time
INSERT INTO reservation_time (start_at) VALUES
                                            ('09:00'), ('10:00'), ('11:00'), ('12:00'), ('13:00'),
                                            ('14:00'), ('15:00'), ('16:00'), ('17:00'), ('18:00');

-- theme
INSERT INTO theme (name, description, thumbnail) VALUES
                                                     ('Mystery Room', 'Solve the mysterious case in 60 minutes', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Prison Break', 'Escape from prison before time runs out', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Zombie Lab', 'Find the cure before zombies take over', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Space Mission', 'Repair the shuttle and return safely', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Haunted House', 'Survive the ghosts and escape', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Spy Academy', 'Pass the spy training missions', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Bank Heist', 'Steal the gold without triggering alarms', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Time Travel', 'Fix the time machine to return', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Alien Invasion', 'Evacuate Earth and contact allies', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp'),
                                                     ('Secret Society', 'Uncover the cult and stop the ritual', 'https://i.namu.wiki/i/zfRtOmLQlnowdqQsfQPPFtjIuZcXI0sXsCYCypGmAGlg4L89S5q7s38sMYwYLdHkV6tYgDwKDIk38StNo_TdaovVMbYKJz46DL3yMjR3VglSFodoABoZQ2VG83vNOMuHuwmsrPlmz6KOot8MbLsNjQ.webp');

-- member
INSERT INTO member (email, password, name, role) VALUES
                                               ('user1@example.com', 'pass1', 'Alice', 'MEMBER'),
                                               ('user2@example.com', 'pass2', 'Bob', 'MEMBER'),
                                               ('user3@example.com', 'pass3', 'Charlie', 'MEMBER'),
                                               ('user4@example.com', 'pass4', 'David', 'MEMBER'),
                                               ('user5@example.com', 'pass5', 'Eve', 'MEMBER'),
                                               ('user6@example.com', 'pass6', 'Frank', 'MEMBER'),
                                               ('user7@example.com', 'pass7', 'Grace', 'MEMBER'),
                                               ('user8@example.com', 'pass8', 'Hannah', 'MEMBER'),
                                               ('user9@example.com', 'pass9', 'Isaac', 'MEMBER'),
                                               ('user10@example.com', 'pass10', 'Jane', 'MEMBER');

-- reservation (조합 생성)
INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES
                                                                 ('2025-05-01', 1, 1, 1),
                                                                 ('2025-05-01', 2, 2, 2),
                                                                 ('2025-05-02', 3, 3, 3),
                                                                 ('2025-05-02', 4, 4, 4),
                                                                 ('2025-05-03', 5, 5, 5),
                                                                 ('2025-05-03', 6, 6, 6),
                                                                 ('2025-05-04', 7, 7, 7),
                                                                 ('2025-05-04', 8, 8, 8),
                                                                 ('2025-05-05', 9, 9, 9),
                                                                 ('2025-05-05', 10, 10, 10);

INSERT INTO member(email, password, name, role)
VALUES ('asd@naver.com', '1234', 'name', 'ADMIN');
