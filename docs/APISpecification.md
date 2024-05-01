## API 명세서

| REQ    | RES             | ENDPOINT               | DESCRIPTION         | FILEPATH                | BODY                                |
|--------|-----------------|------------------------|---------------------|-------------------------|-------------------------------------|
| GET    |                 | /admin                 | 어드민 메인 페이지 접근       | /admin/index.html       |                                     |
| GET    |                 | /admin/reservation-new | 어드민 예약 페이지 접근       | /admin/reservation.html |                                     |
| GET    |                 | /admin/time            | 어드민 시간 페이지 접근       | /admin/time.html        |                                     |
| GET    |                 | /admin/theme           | 어드민 테마 페이지 접근       | admin/theme.html        |                                     |
| GET    |                 | /reservations          | 모든 예약 조회            |                         |                                     |
|        | 200 OK          |                        | 모든 예약 조회            |                         | {id, name, date, time{id, startAt}} |
| POST   |                 | /reservations          | 예약 추가               |                         | name, date, timeId                  |
|        | 201 CREATED     |                        | 예약 추가 성공            |                         | id, name, date, time{id, startAt}   |
|        | 400 BAD REQUEST |                        | 입력 양식으로 인한 예약 추가 실패 |                         | error message                       |
|        | 204 NO CONTENT  |                        | 예약 삭제 성공            |                         |                                     |
|        | 404 NOT FOUND   |                        | 예약 삭제 실패            |                         |                                     |
| GET    |                 | /times                 | 모든 시간 조회            |                         |                                     |
|        | 200 OK          |                        | 모든 시간 조회            |                         | {id, startAt}                       |
| POST   |                 | /times                 | 시간 추가               |                         | startAt                             |
|        | 201 CREATED     |                        | 시간 추가 성공            |                         | id, startAt                         |
|        | 400 BAD REQUEST |                        | 입력 양식으로 인한 시간 추가 실패 |                         | error message                       |
|        | 400 BAD REQUEST |                        | 중복으로 인한 시간 추가 실패    |                         | error message                       |
| DELETE |                 | /times/{id}            | 시간 삭제               |                         |                                     |
|        | 204 NO CONTENT  |                        | 시간 삭제 성공            |                         |                                     |
|        | 404 NOT FOUND   |                        | 시간 삭제 실패            |                         |                                     |
| POST   |                 | /themes                | 테마 추가               |                         | {name, description, thumbnail}      |
|        | 201 CREATED     | /themes/1              | 테마 추가 성공            |                         | {id, name, description, thumbnail}  |
