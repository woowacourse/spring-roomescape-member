# API 명세서

## 관리자 - 테마 추가

<details>
<summary>Request</summary>
<div markdown="1">

```
POST /themes HTTP/1.1
Content-Type: application/json

{
    "name": "블루룸",
    "description": "깊은 바닷속 미스터리를 풀어라. 60분 안에 탈출하지 못하면 영원히 갇힌다.",
    "thumbnail": "https://via.placeholder.com/150"
}
```

</div>
</details>

<details>
<summary>Response</summary>
<div markdown="1">

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "블루룸",
    "description": "깊은 바닷속 미스터리를 풀어라. 60분 안에 탈출하지 못하면 영원히 갇힌다.",
    "thumbnail": "https://via.placeholder.com/150"
}
```

</div>
</details>

<details>
<summary>Exception</summary>
<div markdown="1">

- [ ] 이미 존재하는 테마면 예외가 발생한다.
    - 상태 코드: T-409-001
    - 메시지: "이미 존재하는 테마입니다."

</div>
</details>

---

## 관리자 - 테마 삭제

<details>
<summary>Request</summary>
<div markdown="1">

```
DELETE /themes/{themeId} HTTP/1.1
```

</div>
</details>

<details>
<summary>Response</summary>
<div markdown="1">

```
HTTP/1.1 204
```

</div>
</details>

<details>
<summary>Exception</summary>
<div markdown="1">

- [ ] 존재하지 않는 테마면 예외가 발생한다.
    - 상태 코드: T-404-002
    - 메시지: "존재하지 않는 테마입니다."

</div>
</details>

---

## 사용자 - 시간 목록 조회

<details>
<summary>Query Parameter</summary>
<div markdown="1">

- themeId (필수・long)
  - 예약하고 싶은 테마의 ID 입니다.
- date (필수・string)
  - 예약하고 싶은 날짜 입니다.
  - `yyyy-MM-dd` 형식입니다.

</div>
</details>

<details>
<summary>Request</summary>
<div markdown="1">

```
GET /times?themeId=1&date=2026-05-08 HTTP/1.1
```

</div>
</details>

<details>
<summary>Response</summary>
<div markdown="1">

```
[
    {
        "timeId": 1,
        "startAt": "14:00",
        "isAvailable": true
    },
    {
        "timeId": 2,
        "startAt": "15:00",
        "isAvailable": true
    }
]
```

</div>
</details>

<details>
<summary>Exception</summary>
<div markdown="1">

- [ ] 존재하지 않는 테마면 예외가 발생한다.
    - 상태 코드: T-404-002
    - 메시지: "존재하지 않는 테마입니다."

- [ ] 날짜 형식이 `yyyy-MM-dd`가 아니면 예외가 발생한다.
  - 상태 코드: T-400-003
  - 메시지: "날짜 형식이 'yyyy-MM-dd'가 아닙니다."

</div>
</details>
