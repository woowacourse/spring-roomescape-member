DELETE
FROM RESERVATION;
DELETE
FROM RESERVATION_TIME;
DELETE
FROM THEME;

ALTER TABLE RESERVATION
    ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE RESERVATION_TIME
    ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE THEME
    ALTER COLUMN ID RESTART WITH 1;

INSERT INTO RESERVATION_TIME (START_AT)
VALUES ('08:00'),
       ('09:10'),
       ('10:20'),
       ('11:30');

INSERT INTO THEME (NAME, DESCRIPTION, THUMBNAIL)
VALUES ('젠틀 먼데이', '지금은 고객이 휴가 중으로 전화를 받을 수 없습니다.', 'https://www.keyescape.co.kr/file/theme_info/42_a.jpg'),
       ('백투더 씬', '선생님, 응급 환자입니다! 서둘러 주세요!', 'https://www.keyescape.co.kr/file/theme_info/40_a.jpg'),
       ('메모리 컴패니', '어서오세요. 메모리 컴패니 입니다.', 'https://www.keyescape.co.kr/file/theme_info/37_a.jpg'),
       ('스티브의 사진', '근데 투어 신청하면 VIP 기억 금고도 부여주는거 맞죠?', 'https://www.keyescape.co.kr/file/theme_info/35_a.jpg'),
       ('머니머니 패키지', '각박한 세상 정신 똑바로 차리고 갈 수 있도록!', 'https://www.keyescape.co.kr/file/theme_info/38_a.jpg');

INSERT INTO RESERVATION (NAME, DATE, THEME_ID, TIME_ID)
VALUES ('새양', DATEADD(D, -3, CURRENT_DATE), 2, 2),
       ('새양', DATEADD(D, -3, CURRENT_DATE), 2, 4),

       ('새양', DATEADD(D, -3, CURRENT_DATE), 3, 2),
       ('새양', DATEADD(D, -2, CURRENT_DATE), 3, 3),
       ('새양', DATEADD(D, -2, CURRENT_DATE), 3, 4),

       ('새양', DATEADD(D, -2, CURRENT_DATE), 5, 3),
       ('새양', DATEADD(D, -1, CURRENT_DATE), 5, 4),

       ('Res 8', DATEADD(D, 1, CURRENT_DATE), 2, 2);
