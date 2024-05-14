INSERT INTO member (name, email, password, role) values ('어드민', 'admin@email.com', 'password', 'ADMIN');
INSERT INTO member (name, email, password, role) values ('사용자1', 'member1@email.com', 'password1', 'MEMBER');
INSERT INTO member (name, email, password, role) values ('사용자2', 'member2@email.com', 'password2', 'MEMBER');
INSERT INTO member (name, email, password, role) values ('사용자3', 'member3@email.com', 'password3', 'MEMBER');

INSERT INTO reservation_time (start_at) VALUES ('09:00');
INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');
INSERT INTO reservation_time (start_at) VALUES ('13:00');
INSERT INTO reservation_time (start_at) VALUES ('14:00');
INSERT INTO reservation_time (start_at) VALUES ('15:00');
INSERT INTO reservation_time (start_at) VALUES ('16:00');
INSERT INTO reservation_time (start_at) VALUES ('17:00');
INSERT INTO reservation_time (start_at) VALUES ('18:00');

INSERT INTO theme (name, description, thumbnail) VALUES ('링', '공포', 'https://www.zerogangnam.com/storage/fXDXCwrwxpUJYIJCLmnCUbwwPNqVSwvJISISPCyc.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('콜러', '공포', 'https://www.zerogangnam.com/storage/Tzt6E8ICK3GP13OGFVQsN4vIyqgvZQ3jca7XnGI9.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('포레스트', '공포', 'https://www.zerogangnam.com/storage/1wwvm1DBzxznQwQTIVrm6M2KcBFnpKp4gpC2blLr.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('나비효과', '판타지', 'https://www.zerogangnam.com/storage/s4ySUWR8sCWdA8wi7I1QhcMHOHJIXRnRAwXtrbya.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('어느겨울밤1', '판타지', 'https://www.zerogangnam.com/storage/jzCBsUxlb0epZkaWtuJNHRqwKxSaZNxxvpllT6Cy.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('어느겨울밤2', '판타지', 'https://www.zerogangnam.com/storage/jzCBsUxlb0epZkaWtuJNHRqwKxSaZNxxvpllT6Cy.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('아이엠', '추리', 'https://www.zerogangnam.com/storage/ozAwE3XDqVMVkiKduVDZJxgViJM06lF0G0BX1F7p.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('제로호텔L', '추리', 'https://www.zerogangnam.com/storage/zJhnLh9vxKHVXCJt8IeiCUHM4Y9zMDv4vQcZxWQQ.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('DONE', '추리', 'https://www.zerogangnam.com/storage/GA4O2XX2u4nxhW7JtsRkFKHdHDFdlbzArexU7Omv.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('헐!', '코믹', 'https://www.zerogangnam.com/storage/kQVlRNXEB3OQQwE5BIOvLgk2RhRuLnruknvcPXct.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('토끼와 거북이', '코믹', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6cL_syJHIrZvLLdQSnhzzQkm2Q0em6iPwbW4UH2J4Aw&s');

INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '1', '1', '1');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '1', '2', '2');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '1', '3', '3');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '2', '4', '1');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '2', '5', '2');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '2', '6', '3');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '3', '7', '1');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '3', '8', '2');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '3', '9', '3');
INSERT INTO RESERVATION (date, time_id, theme_id, member_id) VALUES ('2024-05-10', '4', '10', '1');
