INSERT INTO member(id, name, email, password, role)
VALUES (1, '프린', 'prin@gmail.com', '1q2w3e4r!@', 'ADMIN'),
       (2, '레모네', 'remone@gmail.com', '1q2w3e4r!@', 'MEMBER'),
       (3, '오리', 'duck@gmail.com', '1q2w3e4r!@', 'MEMBER');

INSERT INTO reservation_time(id, start_at)
VALUES (1, '10:00'),
       (2, '11:00'),
       (3, '12:00');

INSERT INTO theme(id, name, description, thumbnail)
VALUES (1, '드림 캐쳐', '드림 캐쳐 설명', 'https://theme1.com'),
       (2, '미스터리룸', '미스터리룸 설명', 'https://theme2.com'),
       (3, '1201호 살인사건', '1201호 살인사건 설명', 'https://theme3.com');
