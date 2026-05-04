## 기능 명세

### 사용자
- [ ] 사용자가 예약한 예약만 조회 가능하다.

<br>

### 관리자
- [ ] 모든 예약 조회 가능하다.
- [ ] 특정 사용자의 예약을 조회 가능하다.
- [ ] 테마를 추가할 수 있다.
- [ ] 테마를 삭제할 수 있다.

## API 명세

### 테마 추가 요청·응답 예시

```markdown
POST /admin/themes HTTP/1.1
Content-Type: application/json

{
    "name": "귀신찾기",
    "description": "2023-08-05",
    "thumbnailUrl": "https://"
}
```

```markdown
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json

{
    "id": 1,
    "name": "귀신찾기",
    "description": "2023-08-05",
    "thumbnailUrl": "https://"
}
```

<br>

### 테마 삭제 예시

```markdown
DELETE /admin/themes/{id} HTTP/1.1
Content-Type: application/json

{}
```
```markdown
HTTP/1.1 204 No Content
Date: Wed, 04 Sep 2024 10:16:04 GMT
Content-Type: application/json

{}
```
