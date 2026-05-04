### 기능명세서

**화면**

- [ ]  사용자는 화면을 통해 예약을 할 수 있다

**1단계**

- [x]  테마는 이름, 설명, 썸네일 이미지 url을 가진다.
- [ ]  모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.
- [x]  예약에 테마 정보를 포함한다.
- [x]  관리자가 테마를 추가할 수 있다.
- [x]  관리자가 아닐경우 예외를 던진다.
- [x]  관리자가 테마를 삭제할 수 있다.
- [x]  존재하지 않는 테마는 삭제할 수 없다.
- [ ]  [추후구현] 예약이 걸려있는 테마는 삭제할 수 없다.

**API 명세서**

```markdown
Theme - 01
API 설명: 관리자가 테마를 추가할 수 있다.
URI: /api/v1/themes
Method: POST
Path Variable:
Query Variable:
RequestBody:
{
"name": String,
"description" : String,
"imgUrl" : String,
"userName" : String,
}
ResponseBody:
{
"id" : Long,
"name": String,
"description" : String,
"imgUrl" : String,
}
Status Code: 201

Theme - 02
API 설명: 관리자가 테마를 삭제할 수 있다.
URI: /api/v1/themes/{id}
Method: DELETE
Path Variable: themeId
Query Variable:
RequestBody:
{
"userName" : String
}
ResponseBody:
Status Code: 204
```
