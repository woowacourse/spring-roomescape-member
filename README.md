# 방탈출 사용자 예약

## 1단계

- 예외 처리 후 적절한 응답 반환 (404, 403.. 등)
- 예외 처리
  - 예약
    - 시간: not null 
    - 날짜: not null
    - 예약자명: not null, not blank
    - 시간 + 날짜
      - 예약 일시가 현재 일시 이후여야 한다.
      - 같은 일시에 예약이 있는 경우
  - 시간
    - not null, 25시, 61분 같은 값
    - 이미 있는 시간인 경우 생성 불가
    - 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

## 2단계
>클라이언트 코드를 수정해야 한다.
> 
- 테마 entity가 추가된다.
  - 테마 table추가 및 reservation테이블 수정(제공되는 sql이용)
  - 기존 reservation entity 코드에 theme 추가
- `/admin/theme` -> `theme.html`반환
- reservation페이지 `reservation-new.html`로 변경
- `/reservations`api 필드에 theme 관련 필드 추가
- 테마 조회, 추가, 삭제 구현
- 클라이언트 코드를 수정해야 한다.
  - `/reservations`의 명세가 바뀌게 될텐데 테마와 관련된 필드와 프론트의 맵핑을 해야함

## 3단계
>클라이언트 코드를 수정해야 한다.

- 예약 조회
  -  `/reservation`요청하면 `reservation.html`반환
  - 날짜와 테마에 따른 예약시간 조회
- 예약 추가
  - 예약 추가 api적절히 수정한다.
- 인기테마
  - 최근 일주일 기준 예약이 많은 테마 10개
  - `/`url요청시 `templates/index.html`반환(인기페이지)

  - API 명세
    - 예약 가능 시간 조회
      - request
      ```
      GET /reservation/times_info
      ```
      - response
      ```
      HTTP/1.1 200
      Content-Type: application/json
      [
        {
          "id": 1,
          "start_at": "11:00",
          "is_reserved": "true"
        }
      ]
      ```

  - 예약 하기
    - request
      ```
      POST /reservation HTTP/1.1
      content-type: application/json
      
      {
      "name": "예약자명",
      "reservation_date": "2025-04-29",
      "theme_id": "1",
      "time_id": "1"
      }
      ```
    - response
      ```
      HTTP/1.1 201
      Location: /reservation/1
      Content-Type: application/json
      ```

  - 인기 테마 조회
    - request
      ```
      GET /theme/popular
      ```
      - response
      ```
      HTTP/1.1 200
      Content-Type: application/json
      [
        {
          "id": 1,
          "name": "레벨2 탈출",
          "description": "우테코 레벨2를 탈출하는 내용입니다.",
          "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        }
      ]
      ```