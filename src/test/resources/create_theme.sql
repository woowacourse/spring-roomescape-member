DELETE
FROM theme;

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO theme (name, description, thumbnail_url, runtime)
VALUES ('공포의 저택', '어두운 저택에 숨겨진 비밀을 찾아 탈출하는 테마', 'https://example.com/themes/haunted-house.png', 60),
       ('시간 여행자', '시간의 균열을 따라 단서를 모아 현재로 돌아오는 테마', 'https://example.com/themes/time-traveler.png', 60),
       ('비밀 연구소', '폐쇄된 연구소에서 실험 기록을 추적하는 테마', 'https://example.com/themes/secret-lab.png', 60);
