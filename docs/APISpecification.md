## API 명세서

| REQ    | RES             | ENDPOINT               | DESCRIPTION         | FILEPATH                | BODY                                                                                            |
|--------|-----------------|------------------------|---------------------|-------------------------|-------------------------------------------------------------------------------------------------|
| GET    |                 | /admin                 | 어드민 메인 페이지 접근       | /admin/index.html       |                                                                                                 |
| GET    |                 | /admin/reservation-new | 어드민 예약 페이지 접근       | /admin/reservation.html |                                                                                                 |
| GET    |                 | /admin/time            | 어드민 시간 페이지 접근       | /admin/time.html        |                                                                                                 |
| GET    |                 | /admin/theme           | 어드민 테마 페이지 접근       | /admin/theme.html       |                                                                                                 |
| GET    |                 | /reservation           | 사용자 예약 페이지 접근       | /reservation.html       |                                                                                                 |
| GET    |                 | /                      | 메인 페이지 접근           | /index.html             |                                                                                                 |
| GET    |                 | /login                 | 로그인 페이지 접근          | /login.html             |                                                                                                 |
| GET    |                 | /reservations          | 모든 예약 조회            |                         |                                                                                                 |
|        | 200 OK          |                        | 모든 예약 조회            |                         | {id, date, member{id, name, email}, time{id, startAt}, theme{id, name, description, thumbnail}} |
| POST   |                 | /reservations          | 사용자 예약 추가           |                         | Header(cookie), date, timeId, themeId                                                           |
|        | 201 CREATED     | /reservations/{id}     | 예약 추가 성공            |                         | id, date, member{id, name, email}, time{id, startAt}, theme{id, name, description, thumbnail}   |
|        | 400 BAD REQUEST |                        | 입력 양식으로 인한 예약 추가 실패 |                         | error message                                                                                   |
| POST   |                 | /admin/reservations    | 관리자 예약 추가           |                         | date, memberId, timeId, themeId                                                                 |
|        | 201 CREATED     | /reservations/{id}     | 예약 추가 성공            |                         | id, date, member{id, name, email}, time{id, startAt}, theme{id, name, description, thumbnail}   |
|        | 400 BAD REQUEST |                        | 입력 양식으로 인한 예약 추가 실패 |                         | error message                                                                                   |
|        | 403 FORBIDDEN   |                        | 권한 없음으로 인한 예약 추가 실패 |                         | error message                                                                                   |
| DELETE |                 | /reservations/{id}     | 예약 삭제               |                         |                                                                                                 |
|        | 204 NO CONTENT  |                        | 예약 삭제 성공            |                         |                                                                                                 |
|        | 404 NOT FOUND   |                        | 예약 삭제 실패            |                         | error message                                                                                   |
| GET    |                 | /times                 | 모든 시간 조회            |                         |                                                                                                 |
|        | 200 OK          |                        | 모든 시간 조회            |                         | {id, startAt}                                                                                   |
| GET    |                 | /times/search          | 예약 여부를 포함한 모든 시간 조회 |                         |                                                                                                 |
|        | 200 OK          |                        | 예약 여부를 포함한 모든 시간 조회 |                         | {id ,startAt, booked}                                                                           |
| POST   |                 | /times                 | 시간 추가               |                         | startAt                                                                                         |
|        | 201 CREATED     | /times/{id}            | 시간 추가 성공            |                         | id, startAt                                                                                     |
|        | 400 BAD REQUEST |                        | 입력 양식으로 인한 시간 추가 실패 |                         | error message                                                                                   |
|        | 400 BAD REQUEST |                        | 중복으로 인한 시간 추가 실패    |                         | error message                                                                                   |
| DELETE |                 | /times/{id}            | 시간 삭제               |                         |                                                                                                 |
|        | 204 NO CONTENT  |                        | 시간 삭제 성공            |                         |                                                                                                 |
|        | 400 BAD REQUEST |                        | 시간 삭제 실패            |                         | error message                                                                                   |
| GET    |                 | /themes                | 모든 테마 조회            |                         |                                                                                                 |
|        | 200 OK          |                        | 모든 테마 조회            |                         | {id, name, description, thumbnail}                                                              |
| GET    |                 | /themes/ranking        | 모든 테마 랭킹순으로 조회      |                         |                                                                                                 |
|        | 200 OK          |                        | 모든 테마 랭킹순으로 조회      |                         | {id, name, description, thumbnail}                                                              |
| POST   |                 | /themes                | 테마 추가               |                         | name, description, thumbnail                                                                    |
|        | 201 CREATED     | /themes/{id}           | 테마 추가 성공            |                         | id, name, description, thumbnail                                                                |
| DELETE |                 | /themes/{id}           | 테마 삭제               |                         |                                                                                                 |
|        | 204 NO CONTENT  |                        | 테마 삭제 성공            |                         |                                                                                                 |
|        | 404 NOT FOUND   |                        | 테마 삭제 실패            |                         | error message                                                                                   |
| POST   |                 | /login                 | 사용자 로그인             |                         | email, password                                                                                 |
|        | 200 OK          |                        | 사용자 로그인             |                         | Header(Set-Cookie, Keep-Alive)                                                                  |
| GET    |                 | /login/check           | 쿠키로 사용자 인식          |                         | Header(cookie)                                                                                  |
|        | 200 OK          |                        | 쿠키로 사용자 인식          |                         | name                                                                                            |
