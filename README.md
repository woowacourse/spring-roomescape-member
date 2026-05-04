# 🚀 사이클1 - 미션 (테마 + 사용자 예약)

> 규칙을 적용하면서 사용자가 직접 예약하는 기능을 만든다.

---

## 1단계: 테마 도메인 추가

- [x] 관리자가 테마를 추가할 수 있다.
    ```
    POST /admin/themes
    ```

  **Request**

    ```json
    {
      "name": "테마명",
      "description": "설명",
      "thumbnail": "image-path"
    }
    ```
  
    **Response (201 Created)**
    
    ```json
    {
      "id": 1,
      "name": "테마명",
      "description": "설명",
      "thumbnail": "image-path"
    }
    ```

- [x] 관리자가 테마를 삭제할 수 있다.
    ```
    DELETE /admin/themes/{id}
    ```

  **Response (204 No Content)**

---

## 2단계: 사용자 예약

- [ ] 전체 테마를 조회할 수 있다.

    ```
    GET /themes
    ```
    
    **Response (200 OK)**
    ```json
    [
      {
        "name": "테마명",
        "description": "설명",
        "thumbnail": "image-path"
      }
    ]
    ```

- [ ] 특정 테마의 예약 시간별 예약 가능 여부를 조회할 수 있다.

    ```
    GET /themes/{id}/available-times?date=2026-05-04
    ```
    
    **Response (200 OK)**
    
    ```json
    [
      {
        "time": "18:00",
        "isAvailable": true
      },
      {
        "time": "21:00",
        "isAvailable": false
      }
    ]
    ```

- [ ] 사용자가 예약할 수 있다.

    ```
    POST /reservations
    ```
    
    **Request**

    ```json
    {
      "name": "어셔",
      "themeId": 1,
      "timeId": 1,
      "date": "2026-05-04"
    }
    ```
    
    **Response (201 Created)**

---

## 3단계: 인기 테마 조회

- [ ] 인기 테마를 조회할 수 있다.
    
    ```
    GET /themes/popular?days=7&limit=10
    ```
    
    **Response (200 OK)**
    
    ```json
    [
      {
        "themeId": 1,
        "name": "테마명",
        "description": "설명",
        "thumbnail": "image-path",
        "rank": 1
      },
      {
        "themeId": 2,
        "name": "테마명2",
        "description": "설명2",
        "thumbnail": "image-path2",
        "rank": 2
      }
    ]
    ```

---

## 막힌 순간

- 저장 성공 후 void / 저장한 결과를 반환할지 (테마 추가, 예약 추가)
- 삭제 성공 시 200과 204 중 어떤 상태 코드를 반환할지
- adminController 따로 만들지 아니면 해당 도메인 내에 넣을지
- DB에_저장된_예약을_API로_조회하면_DB_건수와_응답_건수가_같다 jsonPath v1에선 Reservation 반환했는데 통과 v2에선 통과 x

---

## 화면: 예약하기 시나리오

```
화면에 방탈출 테마들을 보여준다.
→ 전체 테마 조회 API

사용자가 테마를 선택한다.

사용자가 날짜를 선택한다.
→ 특정 테마의 예약 시간별 예약 가능 조회 API

사용자는 예약 가능한 시간을 선택한다.
사용자의 이름을 입력한다.

사용자는 예약하기를 누른다.
→ 예약하기 API
```