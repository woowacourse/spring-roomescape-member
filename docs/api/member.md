# 멤버 API 목록

## 회원 가입 API

### Request

```
POST /members HTTP/1.1
content-type: application/json

{
    "password": "password",
    "email": "admin@email.com",
    "name": "노랑"
}
```

### Response

```
HTTP/1.1 201
Location: /members/1
Content-Type: application/json

{
    "id": 1,
    "email": "admin@email.com",
    "name": "노랑"
}
```
