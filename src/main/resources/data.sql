MERGE INTO reservation_time(start_at) KEY (start_at) VALUES
    ('10:00'),
    ('11:00'),
    ('12:00'),
    ('13:00'),
    ('14:00'),
    ('15:00'),
    ('16:00'),
    ('17:00'),
    ('18:00'),
    ('19:00'),
    ('20:00');

MERGE INTO theme(name, description, thumbnail) KEY (name, description, thumbnail) VALUES
    ('시간조작자 연구소', '반복되는 1시간, 탈출 못하면 다시 시작된다.', '/images/themes/1.png'),
    ('사라진 개발자', '흔적만 남긴 채 사라진 개발자의 마지막 로그를 추적', '/images/themes/2.png'),
    ('404호의 비밀', '존재하지 않는 방, 들어간 사람은 돌아오지 않는다.', '/images/themes/3.png'),
    ('녹화된 마지막 하루', '누군가의 마지막 하루를 되돌려 탈출해야 한다.', '/images/themes/4.png'),
    ('VIP 전용 금고', '제한 시간 내 금고를 열지 못하면 시스템이 초기화된다.', '/images/themes/5.png');