INSERT INTO reservation_time (start_at)
values ('09:00');
INSERT INTO reservation_time (start_at)
values ('10:00');
INSERT INTO reservation_time (start_at)
values ('11:00');
INSERT INTO reservation_time (start_at)
values ('12:00');
INSERT INTO reservation_time (start_at)
values ('13:00');
INSERT INTO reservation_time (start_at)
values ('14:00');
INSERT INTO reservation_time (start_at)
values ('15:00');
INSERT INTO reservation_time (start_at)
values ('16:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('드림 이스케이프', '바야흐로, 우테코였다.', 'thumb1.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('블라인드', '아무것도 보이지 않는 우테코로 잡혀온 당신.', 'thumb2.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('미스터리 거울의 방', '친구들과 함께 우테코에 가던 중 갑자기 내린 폭우!', 'thumb3.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('B아파트13동1313호', '우테코가 끝나는 순간, 공포는 현실이 된다.', 'thumb4.jpg');
INSERT INTO theme (name, description, thumbnail)
VALUES ('꽁노리', '이따 포비랑 리사랑 꽁을 차기로 햇따.', 'thumb5.jpg');

INSERT INTO member (name, email, password)
VALUES ('이뜽연', 'forarium20@gmail.com', 'password');
INSERT INTO member (name, email, password)
VALUES ('이주연', 'leejuyeon28@gmail.com', 'password');
INSERT INTO member (name, email, password)
VALUES ('김영훈', 'younghooni@gmail.com', 'password');
INSERT INTO member (name, email, password, role)
VALUES ('관리자', 'admin@gmail.com', 'password', 'ADMIN');
