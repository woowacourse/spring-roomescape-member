# API 명세서
## 관리자 기능
### 관리자 테마 추가
<details>
<summary>Request</summary>
<div markdown="1">

```
POST /reservations HTTP/1.1
Content-Type: application/json

{
"name": "브라운",
"date": "2023-08-05",
"time": "15:40"
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
"name": "브라운",
"date": "2023-08-05",
"time": "15:40"
}
```

</div>
</details>

<details>
<summary>Exception</summary>
<div markdown="1">

- [ ] 이미 존재하는 테마이면 예외 발생

</div>
</details>
