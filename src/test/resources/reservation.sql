INSERT INTO reservation_time
VALUES (1L, '11:10');
INSERT INTO reservation_time
VALUES (2L, '12:10');
INSERT INTO reservation_time
VALUES (3L, '13:10');

-- todo: 나중에 한번에 넣는 방식으로 수정
INSERT INTO theme
VALUES (1L, '우와1`', '재밌는설명', '썸네일');
INSERT INTO theme
VALUES (2L, '오오1', '설명22', '썸네일2');
INSERT INTO theme
VALUES (3L, '굿굿1', '설명33', '썸네일3');

-- 테마 1, 테마 3, 테마 2 순으로 예약이 많이 되었다.
INSERT INTO reservation
VALUES (1L, '프린', '2024-05-01', 1L, 1L);
INSERT INTO reservation
VALUES (2L, '프린', '2024-05-01', 2L, 1L);
INSERT INTO reservation
VALUES (3L, '프린', '2024-05-02', 1L, 1L);
INSERT INTO reservation
VALUES (4L, '프린', '2024-05-02', 2L, 1L);
INSERT INTO reservation
VALUES (5L, '프린', '2024-05-02', 3L, 1L);
INSERT INTO reservation
VALUES (6L, '프린', '2024-05-03', 1L, 2L);
INSERT INTO reservation
VALUES (7L, '프린', '2024-05-03', 2L, 2L);
INSERT INTO reservation
VALUES (8L, '프린', '2024-05-03', 3L, 3L);
INSERT INTO reservation
VALUES (9L, '프린', '2024-05-04', 1L, 3L);
INSERT INTO reservation
VALUES (10L, '프린', '2024-05-04', 2L, 3L);
