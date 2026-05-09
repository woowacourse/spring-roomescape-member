MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('11:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('12:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('13:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('14:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('15:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('16:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('17:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('18:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('19:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('20:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('21:00:00');
MERGE INTO reservation_time (start_at) KEY (start_at) VALUES ('22:00:00');

MERGE INTO theme (name, description, thumbnail_url, is_active) KEY (name)
    VALUES ('잠겨버린 연구실', '제한 시간 안에 단서를 찾아 연구실을 탈출해야 합니다.',
    'https://images.unsplash.com/photo-1518005020951-eccb494ad742', true);

MERGE INTO theme (name, description, thumbnail_url, is_active) KEY (name)
    VALUES ('사라진 탐정', '실종된 탐정의 흔적을 따라 사건의 진실을 밝혀내세요.',
    'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee', true);

MERGE INTO theme (name, description, thumbnail_url, is_active) KEY (name)
    VALUES ('고대 유적의 비밀', '고대 유적에 숨겨진 암호를 풀고 보물을 찾아야 합니다.',
    'https://images.unsplash.com/photo-1506744038136-46273834b3fb', true);

MERGE INTO theme (name, description, thumbnail_url, is_active) KEY (name)
    VALUES ('유령 호텔', '폐쇄된 호텔에서 벌어진 미스터리한 사건을 해결하세요.',
    'https://images.unsplash.com/photo-1566073771259-6a8506099945', true);

INSERT INTO reservation (name, date, start_at, theme_id, status)
VALUES
    ('김민준', DATEADD('DAY', 0, CURRENT_DATE), '11:00:00',
     (SELECT id FROM theme WHERE name = '잠겨버린 연구실'), 'RESERVED'),
    ('이서연', DATEADD('DAY', 0, CURRENT_DATE), '12:00:00',
     (SELECT id FROM theme WHERE name = '잠겨버린 연구실'), 'RESERVED'),
    ('박지후', DATEADD('DAY', 0, CURRENT_DATE), '13:00:00',
     (SELECT id FROM theme WHERE name = '사라진 탐정'), 'RESERVED'),
    ('최하은', DATEADD('DAY', -1, CURRENT_DATE), '11:00:00',
     (SELECT id FROM theme WHERE name = '잠겨버린 연구실'), 'RESERVED'),
    ('정도윤', DATEADD('DAY', -1, CURRENT_DATE), '14:00:00',
     (SELECT id FROM theme WHERE name = '고대 유적의 비밀'), 'RESERVED'),
    ('한지민', DATEADD('DAY', -1, CURRENT_DATE), '15:00:00',
     (SELECT id FROM theme WHERE name = '사라진 탐정'), 'RESERVED'),
    ('윤서준', DATEADD('DAY', -1, CURRENT_DATE), '16:00:00',
     (SELECT id FROM theme WHERE name = '잠겨버린 연구실'), 'RESERVED'),
    ('오지아', DATEADD('DAY', -2, CURRENT_DATE), '17:00:00',
     (SELECT id FROM theme WHERE name = '잠겨버린 연구실'), 'RESERVED'),
    ('강민재', DATEADD('DAY', -2, CURRENT_DATE), '18:00:00',
     (SELECT id FROM theme WHERE name = '고대 유적의 비밀'), 'RESERVED'),
    ('신예린', DATEADD('DAY', -3, CURRENT_DATE), '11:00:00',
     (SELECT id FROM theme WHERE name = '사라진 탐정'), 'RESERVED'),
    ('송우석', DATEADD('DAY', -3, CURRENT_DATE), '19:00:00',
     (SELECT id FROM theme WHERE name = '잠겨버린 연구실'), 'RESERVED'),
    ('장하준', DATEADD('DAY', -3, CURRENT_DATE), '20:00:00',
     (SELECT id FROM theme WHERE name = '유령 호텔'), 'RESERVED'),
    ('임수아', DATEADD('DAY', -4, CURRENT_DATE), '12:00:00',
     (SELECT id FROM theme WHERE name = '잠겨버린 연구실'), 'RESERVED'),
    ('문지호', DATEADD('DAY', -4, CURRENT_DATE), '13:00:00',
     (SELECT id FROM theme WHERE name = '고대 유적의 비밀'), 'RESERVED'),
    ('백서윤', DATEADD('DAY', -4, CURRENT_DATE), '21:00:00',
     (SELECT id FROM theme WHERE name = '유령 호텔'), 'CANCELED');
